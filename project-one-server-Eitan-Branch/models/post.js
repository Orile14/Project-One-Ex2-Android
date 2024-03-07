const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Post = new Schema({
    _id: {
        type: String,
    },
    postOwnerID: {
        type: String,
        required: true
    },
    content: {
        type: String,
        required: false
    },
    img: {
        type: String,
        required: false
    },
    date: {
        type: Date,
        default: Date.now
    },
    comments: {
        type: [{
            _id: {
                type: String,
            }, commentOwnerID: {
                type: String,
                required: true
            },
            nickname:{
                type: String,
                required: false
            },
            profilePic:{
                type: String,
                required: false
            },
            content: {
                type: String,
                required: false
            },
            date: {
                type: Date,
                default: Date.now
            },
            likes: {
                type: [String],
                required: false
            }
        }],
        required: false
    },
    likesID: {
        type: [String],
        required: false
    }
});

module.exports = mongoose.model('Post', Post);