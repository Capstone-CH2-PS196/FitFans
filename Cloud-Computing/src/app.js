const Hapi = require("@hapi/hapi");
const { loadModel, predict } = require("./ml");
const { getUsers, addUser, editUserById, editUserByEmail, deleteUser, getUserById, getUsersByEmail } = require("./sql");
const dotenv = require("dotenv");
dotenv.config();

(async () => {
  // load and get machine learning model
  const model = await loadModel();
  console.log("model loaded!");

  // initializing HTTP server
  const server = Hapi.server({
    host: process.env.NODE_ENV !== "production" ? "localhost" : "0.0.0.0",
    port: 3000,
  });

  // Endpoint untuk mendapatkan data dari tabel users
  server.route({
    method: "GET",
    path: "/users",
    handler: async (request, h) => {
      try {
        const userId = request.query.user_id;
        const userEmail = request.query.user_email;

        let users;

        if (userId !== undefined && userEmail !== undefined) {
          // Get by ID and Email
          return h.response({ error: "Provide either user_id or user_email, not both." }).code(400);
        } else if (userId !== undefined) {
          // Get by ID
          users = await getUserById(userId);
        } else if (userEmail !== undefined) {
          // Get by Email
          users = await getUsersByEmail(userEmail);
        } else {
          // Get all users
          users = await getUsers();
        }

        // Kembalikan data pengguna sebagai respons
        return h.response(users).code(200);
      } catch (err) {
        console.error("Error getting users from Google Cloud SQL:", err);
        return h.response({ error: "Internal Server Error" }).code(500);
      }
    },
  });

  // Endpoint untuk menambahkan pengguna baru
  server.route({
    method: "POST",
    path: "/users",
    handler: async (request, h) => {
      try {
        const newUser = request.payload;

        // Validasi minimal pada data yang dikirimkan
        if (!newUser || !newUser.full_name || !newUser.age || !newUser.weight || !newUser.height || !newUser.gender || !newUser.email) {
          return h.response({ error: "Bad Request - Invalid User Data" }).code(400);
        }

        const result = await addUser(newUser);

        return h.response(result).code(201);
      } catch (err) {
        console.error("Error adding user to Google Cloud SQL:", err);
        return h.response({ error: "Internal Server Error" }).code(500);
      }
    },
    options: {
      payload: {
        allow: "application/json",
      },
    },
  });

  // Endpoint untuk mengedit pengguna berdasarkan user_id atau email
  server.route({
    method: "PUT",
    path: "/users/{identifier}",
    handler: async (request, h) => {
      try {
        const identifier = request.params.identifier;
        const updatedUser = request.payload;

        // Validasi minimal pada data yang dikirimkan
        if (!updatedUser || !updatedUser.full_name || !updatedUser.age || !updatedUser.weight || !updatedUser.height || !updatedUser.gender || !updatedUser.email) {
          return h.response({ error: "Bad Request - Invalid User Data" }).code(400);
        }

        let result;

        // Menentukan apakah identifier berupa user_id atau email
        if (identifier.includes("@")) {
          result = await editUserByEmail(identifier, updatedUser);
        } else {
          result = await editUserById(identifier, updatedUser);
        }

        return h.response(result).code(200);
      } catch (err) {
        console.error("Error editing user in Google Cloud SQL:", err);
        return h.response({ error: "Internal Server Error" }).code(500);
      }
    },
    options: {
      payload: {
        allow: "application/json",
      },
    },
  });

  // Endpoint untuk menghapus pengguna
  server.route({
    method: "DELETE",
    path: "/users/{userId}",
    handler: async (request, h) => {
      try {
        const userId = request.params.userId;
        const result = await deleteUser(userId);

        return h.response(result).code(204);
      } catch (err) {
        console.error("Error deleting user from Google Cloud SQL:", err);
        return h.response({ error: "Internal Server Error" }).code(500);
      }
    },
  });

  // Endpoint untuk melakukan prediksi
  server.route({
    method: "POST",
    path: "/predicts",
    handler: async (request) => {
      // get image that uploaded by user
      const { image } = request.payload;
      // do and get prediction result by giving model and image
      const predictions = await predict(model, image);

      const usersData = await getUsers(); // get data from database

      // Check if the maximum value in predictions is above a certain threshold
      const threshold = 0.5; // You can adjust this threshold based on your needs
      const maxIndex = predictions.indexOf(Math.max(...predictions));
      if (predictions[maxIndex] > threshold) {
        switch (maxIndex) {
          case 0:
            return { result: "barbell" };
          case 1:
            return { result: "dumbbell" };
          case 2:
            return { result: "gym-ball" };
          case 3:
            return { result: "kettlebell" };
          case 4:
            return { result: "leg-press" };
          case 5:
            return { result: "punching-bag" };
          case 6:
            return { result: "roller-abs" };
          case 7:
            return { result: "static-bicycle" };
          case 8:
            return { result: "step" };
          case 9:
            return { result: "treadmill" };
          default:
            return { result: "Gym equipment is not yet available" };
        }
      } else {
        return { result: "No gym equipment detected" };
      }
    },
    // make request payload as `multipart/form-data` to accept file upload
    options: {
      payload: {
        allow: "multipart/form-data",
        multipart: true,
      },
    },
  });

  // running server
  await server.start();

  console.log(`Server start at: ${server.info.uri}`);
})();
