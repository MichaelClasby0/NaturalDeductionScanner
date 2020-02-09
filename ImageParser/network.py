import gzip
import struct

import numpy as np
from tensorflow_core.python.keras.engine.sequential import Sequential
from tensorflow_core.python.keras.layers.convolutional import Conv2D
from tensorflow_core.python.keras.layers.core import Dropout
from tensorflow_core.python.keras.layers.core import Flatten, Dense
from tensorflow_core.python.keras.layers.pooling import MaxPooling2D
from tensorflow_core.python.keras.optimizer_v2.adam import Adam
from tensorflow_core.python.keras.utils.np_utils import to_categorical

from load_connectives import get_log_symb


def read_idx(filename):
    with gzip.open(filename) as f:
        zero, data_type, dims = struct.unpack('>HBB', f.read(4))
        shape = tuple(struct.unpack('>I', f.read(4))[0] for d in range(dims))
        return np.fromstring(f.read(), dtype=np.uint8).reshape(shape)


connectives_x, connectives_y = get_log_symb()

train_images = read_idx('data/characters/emnist-letters-train-images-idx3-ubyte.gz')
train_digits = read_idx('data/characters/train-images-idx3-ubyte.gz')

# test_images = read_idx('data/characters/emnist-letters-test-images-idx3-ubyte.gz')

# Rotate images to proper orientation
for x in range(len(train_images)):
    train_images[x] = np.rot90(np.fliplr(train_images[x]))

# for x in range(len(test_images)):
#     test_images[x] = np.rot90(np.fliplr(test_images[x]))

train_images = np.concatenate((train_images, connectives_x), 0)
train_images = np.concatenate((train_images, train_digits), 0)

# Reshape images to fit model requirements
train_images = train_images.reshape((train_images.shape[0], train_images.shape[1], train_images.shape[2], 1))
# test_images = test_images.reshape((test_images.shape[0], test_images.shape[1], test_images.shape[2], 1))

# Shift labels 0 - A, 25 - Z
train_labels = read_idx('data/characters/emnist-letters-train-labels-idx1-ubyte.gz')
digit_train_labels = read_idx('data/characters/train-labels-idx1-ubyte.gz')
train_labels[:] = [float(x - 1) for x in train_labels]
digit_train_labels[:] = [float(x - 1) + 34 for x in digit_train_labels]
train_labels = np.concatenate((train_labels, connectives_y))
train_labels = np.concatenate((train_labels, digit_train_labels))
# test_labels = read_idx('data/characters/emnist-letters-test-labels-idx1-ubyte.gz')
# test_labels[:] = [x - 1 for x in test_labels]

train_labels = to_categorical(train_labels)
# test_labels = to_categorical(test_labels)

#
# # Note that the validation data should not be augmented!
# val_datagen = ImageDataGenerator(rescale=1. / 255)
#
# train_generator = train_datagen.flow(
#     train_images,
#     train_labels,
#     batch_size=128)
#
# validation_generator = val_datagen.flow(
#     test_images,
#     test_labels,
#     batch_size=128)

model = Sequential()
model.add(Conv2D(16, 3, activation='relu', input_shape=(28, 28, 1)))
model.add(MaxPooling2D(2, 1))
model.add(Dropout(0.3))
model.add(Conv2D(32, 3, activation='relu'))
model.add(MaxPooling2D(2, 1))
model.add(Conv2D(64, 3, activation='relu'))
model.add(Dropout(0.3))
model.add(Flatten())
model.add(Dense(128, activation='relu'))
model.add(Dropout(0.3))
model.add(Dense(43, activation='softmax'))

model.compile(optimizer=Adam(), loss='categorical_crossentropy', metrics=['accuracy'])

model.fit(train_images, train_labels, batch_size=32, epochs=15)

#results = model.evaluate(trai, test_labels)

model.save("weights.h5")
