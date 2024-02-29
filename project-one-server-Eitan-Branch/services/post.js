const Post = require('../models/post')
const User = require('../models/user')
const mongoose = require('mongoose');

const createPost = async (postOwnerID, content, img, date, comments, likesID) => {
    const randomId = new mongoose.Types.ObjectId();
    const post = new Post({ _id: randomId, postOwnerID, content, img, date, comments, likesID });
    const user = await User.findById(postOwnerID);
    user.posts.push(randomId);
    await user.save();
    return await post.save();
};

const getPostById = async (id) => {
    return await Post.findById(id);
}

const getPosts = async () => {    
    return await Post.find({});
}
const deletePost = async (post) => {
    return await post.deleteOne();
}

const likePost = async (postId, userId) => {
    const post = await getPostById(postId)
    if (!post) return null
    if (post.likesID.includes(userId)) {
        post.likesID = post.likesID.filter(id => id !== userId)
    } else {
        post.likesID.push(userId)
    }
    await post.save();
    return post;
}
const updatePost = async (post, content) => {
    if (!post) return null
    post.content = content
    await post.save();
    return post
}
const updateImage = async (post, img) => {
    console.log(post)
    post.img = img
    console.log(post)
    await post.save();
    return post
}
const getFriendsPosts = async (userId) => {
    const user = await User.findById(userId);
    const friendPosts = await Post.find({ postOwnerID: { $in: user.friends } })
                                  .sort({ date: -1 })
                                  .limit(20);

    const nonFriendPosts = await Post.find({ postOwnerID: { $nin: user.friends } })
                                     .sort({ date: -1 })
                                     .limit(5);

    let combinedPosts = [...friendPosts, ...nonFriendPosts];
    combinedPosts = combinedPosts.sort((a, b) => b.date - a.date);

    const postsWithProfile = await Promise.all(combinedPosts.map(async (post) => {
        const postOwner = await User.findById(post.postOwnerID);
        return {
            ...post.toObject(), // Converts the Mongoose document to a plain JavaScript object
            profilePic: postOwner.img,
            nick: postOwner.nick
        };
    }));

    return postsWithProfile;
};



const addComment = async (commentOwnerID, content, post) => {
    const randomId = new mongoose.Types.ObjectId();
    const date = new Date();
    const likes = [];
    const newComment = { _id: randomId, commentOwnerID, content, date, likes };

    await Post.updateOne(
        { _id: post._id },
        { $push: { comments: newComment } }
    );

    // Fetch the updated post to get the latest comments
    const updatedPost = await Post.findById(post._id);
    return updatedPost.comments;
};


const updateComment = async (commentId,post, content) => {
    if (!post) return null
    const commentIndex = post.comments.find(comment => comment._id == commentId);
    if (!commentIndex) return null
    commentIndex.content = content
    await post.save();
    return post.comments
}
const likeComment = async (postId, commentId, userId) => {
    const post = await getPostById(postId)
    if (!post) return null
    const comment = post.comments.find(comment => comment._id == commentId)
    if (!comment) return null
    if (comment.likes.includes(userId)) {
        comment.likes = comment.likes.filter(id => id !== userId)
    } else {
        comment.likes.push(userId)
    }
    await post.save();
    return comment.likes;
}
const getCommentById = async (post, commentId) => {
    if (!post) return null
    return post.comments.find(comment => comment._id == commentId);
}
const deleteComment = async (postId, commentId, userId) => {
    try {
        const post = await Post.findById(postId);
        if (!post) {
            return { status: 404, error: 'Post not found' };
        }
        const commentIndex = post.comments.find(comment => comment._id == commentId);
        if (!commentIndex) {
            return { status: 404, error: 'Comment not found' };
        }
        if (commentIndex.commentOwnerID != userId) {
            return { status: 403, error: 'Unauthorized' };
        }
        post.comments = post.comments.filter(comment => comment._id.toString() !== commentId);
        await post.save();
        return { status: 200, post: post }; 

    } catch (error) {
        console.error('Error deleting comment:', error);
        return { status: 500, error: 'Internal server error' };
    }
};



module.exports = { createPost, getPosts, getPostById, updatePost, likePost, deletePost, updateImage,
     addComment,deleteComment ,updateComment,likeComment, getFriendsPosts}