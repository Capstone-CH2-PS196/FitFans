const tfjs = require("@tensorflow/tfjs-node");
const dotenv = require("dotenv");
dotenv.config();

function loadModel() {
  const modelUrl = `https://storage.googleapis.com/bucket-capstone-ch2-ps196/model.json`;
  return tfjs.loadLayersModel(modelUrl);
}

function predict(model, imageBuffer) {
  const tensor = tfjs.node.decodeJpeg(imageBuffer).resizeNearestNeighbor([150, 150]).expandDims().toFloat();
  // Normalisasi: Bagi setiap nilai dengan 255
  tensor.div(tfjs.scalar(255));
  return model.predict(tensor).data();
}

module.exports = { loadModel, predict };
