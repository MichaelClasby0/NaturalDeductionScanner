import os

import numpy as np
from PIL import Image


def get_log_symb():
    lst_img = []
    lst_labels = []
    for cons in os.listdir("data/connectives/"):
        filename = os.fsdecode(cons)
        for i in os.listdir("data/connectives/" + cons):
            img = Image.open("data/connectives/" + cons + "/" + i)
            new_img = img.resize((28, 28))
            lst_img.append(np.asarray(new_img))
            if filename == "and":
                lst_labels.append(26)
            elif filename == "not":
                lst_labels.append(27)
            elif filename == "iff":
                lst_labels.append(28)
            elif filename == "if":
                lst_labels.append(29)
            elif filename == "false":
                lst_labels.append(30)
            elif filename == "commas":
                lst_labels.append(31)
            elif filename == "lBracket":
                lst_labels.append(32)
            elif filename == "rBracket":
                lst_labels.append(33)
    return np.array(lst_img), np.array(lst_labels)

