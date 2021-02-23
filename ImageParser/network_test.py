import numpy as np
import tensorflow as tf
from PIL import Image
import os


def processPrediction(predictions):
    index = 0
    for p in predictions:
        if p > 0.25:
            if index <= 25:
                print(chr(ord("A") + index))
            elif index == 26:
                print("and")
            elif index == 27:
                print("not")
            elif index == 28:
                print("iff")
            elif index == 29:
                print("if")
            elif index == 30:
                print("false")
            elif index == 31:
                print("commas")
            elif index == 32:
                print("left Bracket")
            elif index == 33:
                print("right Bracket")

        index += 1



for x in range(6):
    img = Image.open("data/temp/ALL - " + str(x) + ".jpg")
    new_img = img.resize((28, 28))
    new_img.save("out.png")
    x_data = np.asarray(new_img).reshape(1, 28, 28, 1)

    model = tf.keras.models.load_model('weights.h5', custom_objects={'leaky_relu': tf.nn.leaky_relu})
    raw_prediction = model.predict(x_data)
    processPrediction(raw_prediction[0])

img = Image.open("data/temp/main - 3.jpg")
new_img = img.resize((28, 28))
new_img.save("out.png")
x_data = np.asarray(new_img).reshape(1, 28, 28, 1)

model = tf.keras.models.load_model('weights.h5', custom_objects={'leaky_relu': tf.nn.leaky_relu})
raw_prediction = model.predict(x_data)
processPrediction(raw_prediction[0])
