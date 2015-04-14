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
       
    console.log("user: "+req.params.username);
    console.log("Latitude: "+req.params.latitude);
    console.log("Longitude: "+req.params.longitude);

    var lat,lon;
    lat = req.params.latitude;
    lon = req.params.longitude;

    //Lookup info using geocoder
    // Reverse Geocoding
    geocoder.reverseGeocode(lat, lon, function ( err, data ) {
      console.dir(data);
     });

    res.send("OK");
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
