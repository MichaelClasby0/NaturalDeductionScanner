import flask
import werkzeug

from main import deconstructImage, parseCharacters

app = flask.Flask(__name__)


@app.route('/logic', methods=['GET', 'POST'])
def handle_request():
    imagefile = flask.request.files['image']
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived image File name : " + imagefile.filename)
    imagefile.save(filename)
    deconstructImage(filename)
    p = parseCharacters()
    print(p)
    return p


app.run(host="0.0.0.0", port=6200, debug=True)
