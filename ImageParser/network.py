import gzip
import struct
from typing import Any
from PIL import Image

import numpy as np
from tensorflow import keras
from tensorflow.keras import layers

from load_connectives import get_logic_symbols


def read_idx(filename: str) -> Any:
    with gzip.open(filename) as f:
        _, _, dims = struct.unpack(">HBB", f.read(4))
        shape = tuple(struct.unpack(">I", f.read(4))[0] for _ in range(dims))
        return np.fromstring(f.read(), dtype=np.uint8).reshape(shape)


repeat_connectives = 5
train_connective_imgs, train_connective_labels = get_logic_symbols()

train_char_imgs = read_idx("data/characters/emnist-letters-train-images-idx3-ubyte.gz")
train_digit_imgs = read_idx("data/characters/digit-train-images-idx3-ubyte.gz")
train_char_labels = read_idx(
    "data/characters/emnist-letters-train-labels-idx1-ubyte.gz"
)
train_digit_labels = read_idx("data/characters/digit-train-labels-idx1-ubyte.gz")

test_char_imgs = read_idx("data/characters/emnist-letters-test-images-idx3-ubyte.gz")
test_digit_imgs = read_idx("data/characters/digit-test-images-idx3-ubyte.gz")
test_char_labels = read_idx("data/characters/emnist-letters-test-labels-idx1-ubyte.gz")
test_digit_labels = read_idx("data/characters/digit-test-labels-idx1-ubyte.gz")

train_char_labels[:] = [float(x - 1) for x in train_char_labels]
train_digit_labels[:] = [float(x) + 34 for x in train_digit_labels]

test_char_labels[:] = [float(x - 1) for x in test_char_labels]
test_digit_labels[:] = [float(x) + 34 for x in test_digit_labels]


def rotate_image_list(img_list):
    for index, image in enumerate(img_list):
        img_list[index] = np.rot90(np.fliplr(image))

for x in range(5):
    train_connective_imgs = np.concatenate(
        (train_connective_imgs, train_connective_imgs), axis=0
    )
    train_connective_labels = np.concatenate(
        (train_connective_labels, train_connective_labels), axis = 0
)


# Rotate images to proper orientation
rotate_image_list(train_char_imgs)
rotate_image_list(test_char_imgs)

train_images = np.concatenate(
    (train_char_imgs, train_digit_imgs, train_connective_imgs), axis=0
)
test_images = np.concatenate((test_char_imgs, test_digit_imgs), axis=0)

# Reshape images to fit model requirements
train_images = train_images.reshape(
    (train_images.shape[0], train_images.shape[1], train_images.shape[2], 1)
)
test_images = test_images.reshape(
    (test_images.shape[0], test_images.shape[1], test_images.shape[2], 1)
)


train_labels = np.concatenate(
    (train_char_labels, train_digit_labels, train_connective_labels), axis=0
)
test_labels = np.concatenate((test_char_labels, test_digit_labels), axis=0)
# Shift labels 0 - A, 25 - Z
catagorical_train_labels = keras.utils.to_categorical(train_labels)
catagorical_test_labels = keras.utils.to_categorical(test_labels)


# Note that the validation data should not be augmented!
val_datagen = keras.preprocessing.image.ImageDataGenerator(rescale=1.0 / 255)

train_generator = val_datagen.flow(
    train_images, catagorical_train_labels, batch_size=128
)
# validation_generator = val_datagen.flow(
#     test_images, catagorical_test_labels, batch_size=128
# )

model = keras.Sequential()
model.add(layers.Conv2D(16, 3, activation="relu", input_shape=(28, 28, 1)))
model.add(layers.MaxPooling2D(2, 1))
model.add(layers.Dropout(0.3))
model.add(layers.Conv2D(32, 3, activation="relu"))
model.add(layers.MaxPooling2D(2, 1))
model.add(layers.Conv2D(64, 3, activation="relu"))
model.add(layers.Dropout(0.3))
model.add(layers.Flatten())
model.add(layers.Dense(128, activation="relu"))
model.add(layers.Dropout(0.3))
model.add(layers.Dense(44, activation="softmax"))

model.compile(
    optimizer=keras.optimizers.Adamax(),
    loss="categorical_crossentropy",
    metrics=["accuracy"],
)

model.summary()

model.fit(
    train_generator,
    batch_size=200,
    epochs=5,
    shuffle=True,
    # validation_data=validation_generator,
)
model.save("weights.h5")