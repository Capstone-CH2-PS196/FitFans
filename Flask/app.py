from flask import Flask, jsonify, request
from tensorflow.keras.preprocessing import image
import numpy as np
import os
import tensorflow as tf

app = Flask(__name__)

# Load pre-trained model
model = tf.keras.models.load_model('Gym_Tools_Multi.h5')

# Nama kelas untuk prediksi
class_names = ['barbell', 'dumbell', 'gym-ball', 'kattle-ball', 'leg-press', 'punching-bag', 'roller-abs', 'statis-bicycle', 'step', 'treadmill']

# Fungsi untuk memprediksi kelas gambar
def predict_image_class(img_path):
    img = image.load_img(img_path, target_size=(150, 150))
    img = image.img_to_array(img)
    img = np.expand_dims(img, axis=0)
    img /= 255.

    predictions = model.predict(img)
    max_probability = np.max(predictions)
    if max_probability < 0.5:
        return "Gambar tidak dikenali"
    else:
        predicted_class = np.argmax(predictions)
        return class_names[predicted_class]

# Endpoint untuk prediksi gambar
@app.route('/predict', methods=['POST'])
def predict_endpoint():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    # Simpan gambar yang diunggah
    upload_path = 'uploads'
    if not os.path.exists(upload_path):
        os.makedirs(upload_path)
    file_path = os.path.join(upload_path, file.filename)
    file.save(file_path)

    # Prediksi kelas untuk gambar yang diunggah
    predicted_class = predict_image_class(file_path)

    # Kembalikan hasil prediksi
    return jsonify({'prediction': predicted_class})

if __name__ == '__main__':
    app.run(debug=True)