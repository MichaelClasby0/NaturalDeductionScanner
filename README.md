# NaturalDeductionScanner

An android app created in 24 hours for ICHACK2020
https://devpost.com/software/naturaldeductionscanner

**What it does**

It allows the user to scan a handwritten natural deduction proof (or import an image already taken) and the app recognises the characters and symbols. These are parsed into a string which is then parsed and verified in further steps to confirm that the proof is correct or to inform the user which line contains a mistake.

**How we built it**

Using Android Studio with kotlin for the front-end and UI/UX. A model trained using tensor-flow in python which was then imported into our app.
