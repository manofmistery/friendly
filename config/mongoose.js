
mongoose = require('mongoose');

module.exports = function() {

 var db = mongoose.connect('mongodb://localhost/friendly');


require('../models/User');
require('../models/Message');
require('../models/Location');

 User = mongoose.model('User');
 Message = mongoose.model('Message');
 Location = mongoose.model('Location');
 return db;

};
