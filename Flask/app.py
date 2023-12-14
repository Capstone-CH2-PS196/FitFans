from flask import Flask, jsonify, request
from tensorflow.keras.preprocessing import image
import numpy as np
import os
import tensorflow as tf

app = Flask(__name__)

model = tf.keras.models.load_model('Gym_Tools_Multi.h5')

# Class names for predictions
class_names = ['barbell', 'dumbell', 'gym-ball', 'kattle-ball', 'leg-press', 'punching-bag', 'roller-abs', 'statis-bicycle', 'step', 'treadmill']

# Define a dictionary containing tool information
tool_info = {
    'barbell': {
        'id': 1,
        'tool_name': 'Barbell',
        'tool_description': 'A long metal bar with weights on both ends.',
        'how_to_use': [
            'Lie on a flat bench with your back pressed against it',
            'Grip the barbell with both hands, slightly wider than shoulder-width apart, palms facing forward',
            'Lift the barbell off the rack, or have a spotter help you with the lift',
            'Lower the barbell to your chest in a controlled manner, keeping your elbows at a 90-degree angle',
            'Exhale and push the barbell back up to the starting position, fully extending your arms',
            'Repeat for the desired number of repetitions, maintaining control and proper form',
            'Ensure a stable grip and engage your chest muscles throughout the movement',
            'Safely rack the barbell when you have completed your set or need a break.',
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
            
    },
    'dumbell': {
        'id': 2,
        'tool_name': 'Dumbbell',
        'tool_description': 'A short barbell with weights on both ends.',
        'how_to_use': [
            'Stand with feet shoulder-width apart, holding a dumbbell in each hand, arms fully extended, and palms facing forward',
            'Keep elbows close to your body and exhale as you curl the dumbbells toward your shoulders, contracting your biceps',
            'Inhale and slowly lower the dumbbells back to the starting position with controlled movement',
            'Repeat for the desired number of repetitions, maintaining proper form and avoiding swinging motions',
            'Adjust the weight of the dumbbells based on your fitness level, gradually increasing as you get stronger.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }

    },
    'gym-ball': {
        'id': 3,
        'tool_name': 'Gym Ball',
        'tool_description': 'A large inflatable ball used for exercise.',
        'how_to_use': [
            'Sit on the gym ball with your feet flat on the floor and shoulder-width apart',
            'Walk your feet forward, allowing the ball to roll under you until your lower back is comfortably supported',
            'Keep your hips lifted and your body in a straight line from head to knees, engaging your core muscles',
            'Perform exercises like crunches by contracting your abdominal muscles, lifting your chest toward the ceiling, and then lowering back down',
            'For stability, try exercises like planks by positioning your hands on the floor and extending your legs behind you on the ball, maintaining a straight body line',
            'Incorporate twists, side bends, and leg raises to target different areas of your core',
            'Ensure controlled movements, focusing on the contraction and extension of your core muscles',
            'Adjust the difficulty by experimenting with different exercises and the level of inflation of the gym ball',
            'Remember to consult with a fitness professional or healthcare provider before starting a new exercise routine, especially if you have any pre-existing health conditions.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'treadmill': {
        'id': 4,
        'tool_name': 'treadmill',
        'tool_description': 'Device generally used for walking, running, or climbing while staying in the same place',
        'how_to_use': [
            'Stand on the treadmill, ensuring your feet are aligned with the sides of the belt',
            'Start the treadmill and set the speed to a comfortable walking pace',
            'Gradually increase the speed or incline if you want a more challenging workout',
            'Maintain an upright posture with a natural stride, swinging your arms for balance',
            'Use the handrails for support if needed, but avoid leaning on them excessively',
            'Monitor your heart rate and adjust the intensity accordingly',
            'To stop, gradually reduce the speed, and wait for the treadmill to come to a complete stop',
            'Cool down by walking at a slower pace and stretching your muscles',
            'Always follow the treadmill safety guidelines and consult with a fitness professional or healthcare provider before starting a new exercise routine, especially if you have any pre-existing health conditions.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'step': {
        'id': 5,
        'tool_name': 'step',
        'tool_description': 'Device with foot pedals that simulate stair climbing',
        'how_to_use': [
            'Stand on the step machine with your feet positioned evenly on the pedals',
            'Adjust the resistance level to your desired intensity',
            'Use the handrails for balance and support, if necessary',
            'Begin stepping in a controlled and rhythmic manner, alternating between your left and right legs',
            'Maintain an upright posture with your core engaged',
            'Experiment with different step patterns, such as stepping forward, backward, or using lateral movements',
            'Increase or decrease the speed and resistance as needed for your fitness level and workout goals',
            'To stop, reduce the speed gradually and wait for the machine to come to a complete stop',
            'Cool down by stepping at a slower pace for a few minutes',
            'Always follow the manufacturer guidelines for the specific step machine you are using and consult with a fitness professional or healthcare provider if you have any concerns or health conditions.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'kattle-ball': {
        'id': 6,
        'tool_name': 'kettlebell',
        'tool_description': 'A cast-iron or steel ball-shaped weight with a handle attached to the top.',
        'how_to_use': [
            'Stand with your feet shoulder-width apart, toes pointed slightly outward',
            'Place a kettlebell on the floor in front of you',
            'Bend at the hips and knees to lower your body, reaching for the kettlebell with both hands',
            'Grasp the kettlebell handle with both hands, keeping your back flat and chest up',
            'Swing the kettlebell between your legs by hinging at the hips',
            'Quickly reverse the motion, driving through your hips and knees, to propel the kettlebell forward',
            'As the kettlebell rises, stand up straight and swing it to chest height, keeping your arms straight',
            'Allow the kettlebell to swing back between your legs, and repeat the motion for the desired number of reps',
            'Maintain a strong core throughout the movement and engage your glutes at the top of the swing',
            'Control the kettlebell throughout the exercise, and avoid overextending your lower back.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'leg-press': {
        'id': 7,
        'tool_name': 'leg Press',
        'tool_description': 'It typically consists of a seat or a bench attached to a sliding platform on which the user places their feet.',
        'how_to_use': [
            'Adjust the Seat: Set the seat position so that your knees are in line with the machine pivot point. Your feet should be flat on the footplate',
            'Select the Weight: Choose a weight that suits your fitness level and goals. Many leg press machines have a weight stack or a mechanism to add weight plates',
            'Position Your Feet: Place your feet hip-width apart on the footplate. Your knees should be aligned with your feet',
            'Check Your Posture: Sit comfortably on the machine with your back against the seat. Ensure your spine is in a neutral position',
            'Grip the Handles: If the machine has handles, grip them for stability. Some machines may have a safety latch that you release before pushing',
            'Push the Platform: Press through your heels, extending your legs fully, until your knees are almost straight but without locking them. Exhale during this pushing phase',
            'Controlled Return: Bend your knees to lower the footplate back down, inhaling during this controlled descent',
            'Repeat: Perform the desired number of repetitions, maintaining controlled and smooth movements',
            'Adjustments: If your machine allows, you can vary your foot placement to target different muscle groups (higher for emphasis on quads, lower for emphasis on hamstrings and glutes)',
            'Cool Down: After completing your leg press workout, cool down by stretching your legs.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'punching-bag': {
        'id': 8,
        'tool_name': 'punching Bag',
        'tool_description': 'A durable, often cylindrical, bag filled with materials like sand or fabric, suspended from a ceiling or mounted on a stand.',
        'how_to_use': [
            'Safety First: Ensure you have proper hand wraps and gloves to protect your hands and wrists',
            'Stance: Stand in a balanced stance with one foot forward and the other back. Keep your knees slightly bent and your weight distributed evenly',
            'Basic Punches:',
            'Jab: A quick, straight punch with your lead hand',
            'Cross: A powerful punch thrown with your rear hand, crossing your body',
            'Hook: A lateral punch with a bent arm, targeting the sides of the bag',
            'Uppercut: A rising punch delivered from below, using your hips for power',
            'Movement: Circle the bag, move laterally, and practice footwork to simulate real fighting scenarios',
            'Combination Punches: String together different punches in combinations to improve coordination and speed',
            'Defense Drills: Practice slipping, bobbing, and weaving to enhance defensive skills',
            'Kicks and Knee Strikes: If the bag allows, incorporate kicks or knee strikes for a full-body workout',
            'Intensity and Speed: Vary the intensity and speed of your punches to mimic different aspects of a real fight',
            'Breathing: Focus on controlled breathing to maintain stamina and composure',
            'Cooldown: After your session, stretch your arms, shoulders, and legs to prevent stiffness.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'roller-abs': {
        'id': 9,
        'tool_name': 'roller Abs',
        'tool_description': 'A fitness device consisting of a wheel with handles on either side.',
        'how_to_use': [
            'Kneel on the floor with the ab wheel in front of you', 
            'Hold the handles of the ab wheel with both hands, keeping your arms extended',
            'Tighten your core muscles to stabilize your spine',
            'Slowly roll the ab wheel forward by extending your arms in front of you',
            'Maintain a straight line from your head to your knees',
            'Roll out as far as you can while keeping control and without letting your lower back sag',
            'Engage your core muscles as you roll the ab wheel back toward your knees', 
            'Keep your movements controlled and avoid using only your arms',
            'Perform the exercise for the desired number of repetitions', 
            'Focus on quality over quantity to avoid strain on your lower back',
            'Modify the difficulty by adjusting the distance you roll out or by incorporating variations, such as rolling at an angle',
            'Exhale as you roll out and inhale as you return to the starting position',
            'Include ab wheel exercises in your regular core workout routine for best results',
            'After completing your ab wheel workout, stretch your core and back muscles.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    },
    'statis-bicycle': {
        'id': 10,
        'tool_name': 'static Bicycle',
        'tool_description': 'A stationary fitness device with pedals and a seat, designed for indoor use to simulate cycling.',
        'how_to_use': [
            'Set the seat height so that your legs are almost fully extended at the bottom of the pedal stroke, with a slight bend in your knees',
            'Position the handlebars at a comfortable height',
            'Get on the bike and place your feet on the pedals',
            'Start pedaling in a controlled manner',
            'Keep your back straight and your core engaged',
            'Relax your shoulders and grip the handlebars lightly',
            'Use the resistance knob or setting to increase or decrease the difficulty of your workout',
            'Experiment with different pedaling speeds, including intervals of faster and slower cadences',
            'If your exercise bike has heart rate monitoring, keep an eye on your heart rate to gauge intensity',
            'Include intervals of higher resistance or faster pedaling for more challenging workouts',
            'Aim for a duration that aligns with your fitness goals, and be consistent in your workouts',
            'Gradually reduce the resistance and pedal at a slower pace for a few minutes to cool down.'
        ],
        'timer_recomendation': {
            'beginner': 10,
            'ideal': 15,
            'expert': 20,
        }
    }
}


# Function to predict the class of an uploaded image
def predict_image_class(img_path):
    img = image.load_img(img_path, target_size=(150, 150))
    img = image.img_to_array(img)
    img = np.expand_dims(img, axis=0)
    img /= 255.

    predictions = model.predict(img)
    max_probability = np.max(predictions)
    if max_probability < 0.5:
        return {"error": "Image not recognized"}
    else:
        predicted_class = np.argmax(predictions)
        tool_name = class_names[predicted_class]

        tool_info_result = tool_info.get(tool_name, {})
        if not tool_info_result:
            return {"error": "Tool information not found"}

        result = {
            'tool_name': tool_info_result.get('tool_name', ''),
            'tool_description': tool_info_result.get('tool_description', ''),
            'how_to_use': tool_info_result.get('how_to_use', []),
            'timer_recomendation': tool_info_result.get('timer_recomendation', ''),
            
        }
        return result

# Endpoint for image prediction
@app.route('/predict', methods=['POST'])
def predict_endpoint():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    # Save the uploaded image
    upload_path = 'uploads'
    if not os.path.exists(upload_path):
        os.makedirs(upload_path)
    file_path = os.path.join(upload_path, file.filename)
    file.save(file_path)

    # Predict the class for the uploaded image
    prediction_result = predict_image_class(file_path)

    # Return the prediction result in the correct order
    return jsonify({
        'tool_name': prediction_result.get('tool_name', ''),
        'tool_description': prediction_result.get('tool_description', ''),
        'how_to_use': prediction_result.get('how_to_use', []),
        'timer_recomendation': prediction_result.get('timer_recomendation', ''),
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)