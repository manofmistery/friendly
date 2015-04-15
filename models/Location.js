
var mongoose = require('mongoose');
Schema = mongoose.Schema;
LocSchema = new Schema({
    user : String,
    latitude: Number,
    longitude: Number,
    address: String,
    date: Date
});
mongoose.model('Location', LocSchema);