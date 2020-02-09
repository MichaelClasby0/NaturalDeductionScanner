import os

import numpy as np
from PIL import Image


def processConnectives():
    dir = 'data/connectives_raw/'
    connectives = [x[0] for x in os.walk(dir)][1:]
    for connective in connectives:
        connectiveName = os.path.basename(connective)
        images = [x[2] for x in os.walk(connective + "/")][0]
        for i in images:
            image = Image.open(dir + connectiveName + "/" + i)
            new_img = image.resize((28, 28))
            try:
                new_img.save("data/connectives/" + connectiveName + "/" + i, "JPEG", optimize=True)
            except FileNotFoundError:
                os.mkdir("data/connectives/" + connectiveName)
                new_img.save("data/connectives/" + connectiveName + "/" + i, "JPEG", optimize=True)


def getConnectives():
    labels = []
    images = []

    dir = 'data/connectives/'
    connectives = [x[0] for x in os.walk(dir)][1:]
    for connective in connectives:
        connectiveName = os.path.basename(connective)
        files = [x[2] for x in os.walk(connective + "/")][0]
        for i in files:
            img = Image.open(dir + connectiveName + "/" + i).convert('L')
            images.append(np.asarray(img))
            labels.append(connectiveName)

    return images, labels
