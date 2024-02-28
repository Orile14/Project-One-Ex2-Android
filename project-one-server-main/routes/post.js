const authenticateToken = require('../middleware/authenticateToken');
const postController = require('../controllers/post');
const userController = require('../controllers/user');

const express = require('express');
var router = express.Router();
router.route('/nickname/:id').get(userController.getUsernickname)
router.route('/profile/:id').get(userController.getUserImage)
router.route('/get').get(postController.getPosts)
router.route('/add').post(authenticateToken, postController.createPost);
router.route('/like/:id').put(authenticateToken, postController.likePost)
router.route('/:id').get(postController.getPost)
        .patch(postController.updatePost)
router.route('/uploadImage/:id').patch(authenticateToken, postController.updateImage)
router.route('/delete/:id').delete(authenticateToken, postController.deletePost)
router.route('/edit/:id').get(authenticateToken, postController.checkIfAuth)
router.route('/edit/:id').patch(authenticateToken, postController.updatePost)

router.route('/comment/edit/:postid/:commentname').get(authenticateToken, postController.checkIfAuthComment)
router.route('/comment/edit/:postid/:commentid').patch(authenticateToken, postController.updateComment)
router.route('/comment/:id').post(authenticateToken, postController.addComment)
router.route('/comment/:postid/:commentid').delete(authenticateToken, postController.deleteComment)
router.route('/comment/like/:postid/:commentid').put(authenticateToken, postController.likeComment)
module.exports = router;