
var mongoose = require('mongoose');
Schema = mongoose.Schema;
userSchema = new Schema({
	username: String,
	password: String,
	favorites : [{ type: Number, ref: 'Movie'}]
});
mongoose.model('User', userSchema);