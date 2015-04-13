
var mongoose = require('mongoose');
Schema = mongoose.Schema;
messageSchema = new Schema({
    body : String,
    author: String,
    date: Date
});
mongoose.model('Message', messageSchema);