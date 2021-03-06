import os
import shutil
from typing import List

import cv2
import numpy as np
import tensorflow as tf
from PIL import Image

model = tf.keras.models.load_model("weights.h5")


class Prediction:
    def __init__(self, confidence: float, name: str):
        self.confidence = confidence
        self.name = name

    def __str__(self):
        return self.name + " " + str(self.confidence)


def processImage(path):
    img = cv2.imread(path)
    kernel = np.ones((3, 3), np.uint8)
    dialate = cv2.erode(img, kernel, iterations=3)
    gray = cv2.cvtColor(dialate, cv2.COLOR_BGR2GRAY)
    blur = cv2.GaussianBlur(gray, (3, 3), 2)
    r, processedImg = cv2.threshold(
        blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU
    )
    # processedImg = cv2.adaptiveThreshold(blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 9, 2)

    # kernel = np.ones((3, 3), np.uint8)
    # processedImg = cv2.morphologyEx(processedImg, cv2.MORPH_OPEN, kernel)
    # processedImg = cv2.dilate(processedImg, kernel, iterations=3)

    # kernel = np.ones((2,2), np.uint8)
    # processedImg = cv2.morphologyEx(processedImg, cv2.MORPH_OPEN, kernel)
    # kernel = np.ones((3, 3), np.uint8)
    # processedImg = cv2.morphologyEx(processedImg, cv2.MORPH_CLOSE, kernel)
    # kernel = np.ones((5, 5), np.uint8)
    # processedImg = cv2.morphologyEx(processedImg, cv2.MORPH_OPEN, kernel)
    # processedImg = cv2.morphologyEx(processedImg, cv2.MORPH_OPEN, kernel)
    # processedImg = cv2.dilate(processedImg, kernel, iterations=2)

    return processedImg, processedImg


def getSortedContours(image):
    contours = cv2.findContours(image.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)[
        0
    ]
    contours.sort(key=lambda c: cv2.boundingRect(c)[0])
    return contours


def deconstructImage(path):
    processedImg, image = processImage(path)
    contours = getSortedContours(processedImg)

    output = image.copy()
    index = 0
    padding = 5
    for contour in contours:
        (x, y, w, h) = cv2.boundingRect(contour)
        if cv2.contourArea(contour) > 2000:
            cv2.rectangle(output, (x, y), (x + w, y + h), (70, 0, 70), 3)
            try:
                symbol = image[
                    y - padding : y + h + padding, x - padding : x + w + padding
                ]
                cv2.imwrite("data/temp/" + str(index) + ".jpg", symbol)
            except:
                pass
            index += 1

    cv2.imwrite("output.png", output)


def processPrediction(predictions: List[int]):
    index = 0
    ps = []
    for p in predictions:
        if index <= 25:
            ps.append(Prediction(p, str(chr(ord("A") + index))))
        elif index == 26:
            ps.append(Prediction(p, "^"))
        elif index == 27:
            ps.append(Prediction(p, "¬"))
        elif index == 28:
            ps.append(Prediction(p, "<->"))
        elif index == 29:
            ps.append(Prediction(p, "->"))
        elif index == 30:
            ps.append(Prediction(p, "False"))
        elif index == 31:
            ps.append(Prediction(p, ","))
        elif index == 32:
            ps.append(Prediction(p, "("))
        elif index == 33:
            ps.append(Prediction(p, ")"))
        elif index <= 43:
            ps.append(Prediction(p, str(index - 34)))

        index += 1

    [print(str(x)) for x in sorted(ps, key=lambda x: x.confidence, reverse=True)[:3]]

    return sorted(ps, key=lambda x: x.confidence, reverse=True)[0].name


def cleanupDir():
    folder = "data/temp"
    for filename in os.listdir(folder):
        file_path = os.path.join(folder, filename)
        try:
            if os.path.isfile(file_path) or os.path.islink(file_path):
                os.unlink(file_path)
            elif os.path.isdir(file_path):
                shutil.rmtree(file_path)
        except Exception as e:
            print("Failed to delete %s. Reason: %s" % (file_path, e))


def parseCharacters():
    x_data = []
    files = os.listdir("data/temp")
    files.sort(key=lambda x: os.path.basename(x))
    for file in files:
        img = Image.open("data/temp/" + file)
        img = img.resize((28, 28))
        split_filename = file.split(".")
        print(split_filename)
        img.save("data/temp/" + split_filename[0] + " - resized." + split_filename[1])
        new_img = np.asarray(img)
        new_img = new_img.reshape((28, 28, 1))
        new_img = new_img / 255
        x_data.append(new_img)

    raw_prediction = model.predict(np.array(x_data))
    return "".join(list(map(processPrediction, raw_prediction)))


cleanupDir()
deconstructImage("data/testing/all3.jpg")
print(parseCharacters())
