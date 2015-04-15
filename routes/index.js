var express = require('express');
var router = express.Router();
var mongoose = require('../config/mongoose');
var passport = require('passport');
var geocoder = require('geocoder');

var isAuthenticated = function(req,res,next){
    if(req.isAuthenticated())
        return next();
    res.redirect('/login');
}

/* GET home page. */
router.get('/', isAuthenticated, function(req, res, next) {
    //chatbot.emit('message', "ChatBot", "User logon: "+req.user.username); //ChatBot
    res.render('index', { title: 'Friendly 0.1', user: req.user.username});
});

/* Client reporting geo location */
router.get('/loc/:username/:latitude/:longitude', function(req,res,next){
       

    var lat,lon;
    lat = req.params.latitude;
    lon = req.params.longitude;

    console.log("user: "+req.params.username);
    console.log("Latitude: "+lat);
    console.log("Longitude: "+lon);


    //Lookup info using geocoder
    // Reverse Geocoding
    geocoder.reverseGeocode(lat, lon, function ( err, data ) {
     // console.dir(data);
        var address = data.results[0].formatted_address;
        console.log(address);

        //Save to DB
        var time = new Date();
        var loc = new Location({
           user: req.params.username,
            latitude: lat,
            longitude: lon,
            address: address,
            date: time
        });

        loc.save(function(err, doc){
            if(err) return next(err);

            res.send(address); //send addresss back to app
        });


     });

});

// Return latest position for user
router.get('/latest/:user', function(req, res, next) {

    //Return latest location update from user.
    Location.findOne({user: req.params.user}, {}, {sort: {'date': -1}}, function (err, doc) {
        res.json(doc);
    });
});

router.get('/login', function(req, res, next){
    res.render('login', { message: req.flash('message')});
});
/* Handle Login POST */
router.post('/login', passport.authenticate('login', {
    successRedirect: '/',
    failureRedirect: '/login',
    failureFlash : true
}));
/* Handle Registration POST */
router.post('/signup', passport.authenticate('signup', {
    successRedirect: '/',
    failureRedirect: '/signup',
    failureFlash : true
}));

router.get('/signup', function(req, res, next){
    res.render('register', {message: req.flash('message')});
});
/* Handle Logout */
router.get('/signout', function(req, res) {
   //chatbot.emit('message', "ChatBot", "User "+req.user.username + " Logged out"); //ChatBot
    req.logout();
    res.redirect('/');
});


/** Get last 10 Messages
 *
 */
router.get('/history', function(req,res,next){
    Message.find().sort({'date':-1}).limit(10).exec(function(err, docs){
        res.json(docs);
    });
});



module.exports = router;
