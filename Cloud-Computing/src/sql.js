const mysql = require("mysql2/promise");

const dotenv = require("dotenv");
dotenv.config();

const config = {
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database:process.env.DB_NAME,
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
};

// Fungsi untuk membuat koneksi ke Google Cloud SQL
async function getConnection() {
  try {
    const pool = mysql.createPool(config);
    const connection = await pool.getConnection();
    return connection;
  } catch (err) {
    console.error("Error creating connection to Google Cloud SQL:", err);
    throw err;
  }
}

// Fungsi untuk mendapatkan pengguna
async function getUsers(userId) {
  try {
    console.log("User ID:", userId);

    const connection = await getConnection();

    let query = "SELECT * FROM users";
    let params = [];

    if (userId !== undefined && userId !== null) {
      query += " WHERE user_id = ?";
      params = [userId];
    }

    console.log("SQL Query:", query, "Parameters:", params);

    const [rows, fields] = await connection.execute(query, params);

    connection.release();

    return rows;
  } catch (err) {
    console.error("Error querying Google Cloud SQL:", err);
    throw err;
  }
}

// Fungsi untuk mendapatkan pengguna berdasarkan ID
async function getUserById(userId) {
  try {
    const connection = await getConnection();

    const [rows, fields] = await connection.execute("SELECT * FROM users WHERE user_id = ?", [userId]);

    connection.release();

    return rows;
  } catch (err) {
    console.error("Error querying Google Cloud SQL:", err);
    throw err;
  }
}

// Fungsi untuk mendapatkan pengguna berdasarkan Email
async function getUsersByEmail(userEmail) {
  try {
    const connection = await getConnection();
    const [rows, fields] = await connection.execute("SELECT * FROM users WHERE email = ?", [userEmail]);
    connection.release();
    return rows;
  } catch (err) {
    console.error("Error querying Google Cloud SQL:", err);
    throw err;
  }
}

// Fungsi untuk menambahkan pengguna baru
async function addUser(user) {
  try {
    const connection = await getConnection();

    // Menambahkan data ke tabel users
    const [result] = await connection.execute("INSERT INTO users (full_name, age, weight, height, gender, email, image, total_calories) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", [
      user.full_name,
      user.age,
      user.weight,
      user.height,
      user.gender,
      user.email,
      user.image,
      user.total_calories,
    ]);

    // Menutup koneksi setelah menambahkan data
    connection.release();
    return result;
  } catch (err) {
    console.error("Error adding user to Google Cloud SQL:", err);
    throw err;
  }
}

// fungsi untuk edit user by id dan email
async function editUserById(userId, updatedUser) {
  try {
    const connection = await getConnection();

    // Mengedit data di tabel users
    const [result] = await connection.execute("UPDATE users SET full_name=?, age=?, weight=?, height=?, gender=?, email=?, image=?, total_calories=? WHERE user_id=?", [
      updatedUser.full_name,
      updatedUser.age,
      updatedUser.weight,
      updatedUser.height,
      updatedUser.gender,
      updatedUser.email,
      updatedUser.image,
      updatedUser.total_calories,
      userId,
    ]);

    // Menutup koneksi setelah mengedit data
    connection.release();
    return result;
  } catch (err) {
    console.error("Error editing user in Google Cloud SQL:", err);
    throw err;
  }
}

// edit by email
async function editUserByEmail(email, updatedUser) {
  try {
    const connection = await getConnection();

    // Mengedit data di tabel users
    const [result] = await connection.execute("UPDATE users SET full_name=?, age=?, weight=?, height=?, gender=?, email=?, image=?, total_calories=? WHERE email=?", [
      updatedUser.full_name,
      updatedUser.age,
      updatedUser.weight,
      updatedUser.height,
      updatedUser.gender,
      updatedUser.email,
      updatedUser.image,
      updatedUser.total_calories,
      email,
    ]);
   // Menutup koneksi setelah mengedit data
   connection.release();
   return result;
 } catch (err) {
   console.error("Error editing user in Google Cloud SQL:", err);
   throw err;
 }
}

    //fungsi edit calories by Id
    async function editUserCaloriesById(userId, newTotalCalories) {
      try {
        const connection = await getConnection();
    
        try {
          // Mengedit data di tabel users, updating only total_calories
          const [result] = await connection.execute("UPDATE users SET total_calories=? WHERE user_id=?", [newTotalCalories, userId]);
    
          // Return the result
          return result;
        } finally {
          // Menutup koneksi setelah mengedit data
          connection.release();
        }
      } catch (err) {
        console.error("Error editing user calories in Google Cloud SQL:", err);
        throw err;
      } 
   }

   //fungsi edit calories by email
   async function editUserCaloriesByEmail(email, newTotalCalories) {
    try {
      const connection = await getConnection();
  
      try {
        // Mengedit data di tabel users, updating only total_calories
        const [result] = await connection.execute("UPDATE users SET total_calories=? WHERE email=?", [newTotalCalories, email]);
  
        // Return the result
        return result;
      } finally {
        // Menutup koneksi setelah mengedit data
        connection.release();
      }
    } catch (err) {
      console.error("Error editing user calories by email in Google Cloud SQL:", err);
      throw err;
    }
  }
  
    
// Fungsi untuk menghapus pengguna
async function deleteUser(userId) {
  try {
    const connection = await getConnection();

    // Menghapus data dari tabel users
    const [result] = await connection.execute("DELETE FROM users WHERE user_id=?", [userId]);

    // Menutup koneksi setelah menghapus data
    connection.release();

    return result;
  } catch (err) {
    console.error("Error deleting user from Google Cloud SQL:", err);
    throw err;
  }
}

// Export fungsi-fungsi agar dapat dipanggil di file lain
module.exports = {
  getUsers,
  addUser,
  editUserById,
  editUserByEmail,
  deleteUser,
  getUserById,
  getUsersByEmail,
  editUserCaloriesById,
  editUserCaloriesByEmail,
};
