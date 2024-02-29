const postService = require('../services/post');

const createPost = async (req, res) => {
    // Use the userId from the request, set by the middleware
    const postOwnerID = req.userId;
    const { content, img, date, comments, likesID } = req.body;

    try {
        const post = await postService.createPost(postOwnerID, content, img, date, comments, likesID);
        res.json(post);
    } catch (error) {
        res.status(500).json({ message: 'Error creating post' });
    }
};

const updateImage = async (req, res) => {
    const post = await postService.getPostById(req.params.id)
    const img = req.body.image;
    console.log(req.body.image)
    if(post.postOwnerID != req.userId){
        return res.status(401).json({ errors: ['Unauthorized'] })
    }
    try{
        const updatedPost = await postService.updateImage(post, img)
        if (!updatedPost) {
            return res.status(500).json({ errors: ['Internal server error'] })
        }
        res.json(updatedPost)
    }catch (error) {
        res.status(500).json({ message: 'Error updating image' });
    }
}



const getPost = async (req, res) => {
    const post = await postService.getPostById(req.params.id)
    if (!post) {
        return res.status(404).json({ errors: ['Post not found'] })
    }
    res.json(post)
}

const getPosts = async (req, res) => {
    res.json(await postService.getPosts());
}

const updatePost = async (req, res) => {
    const post = await postService.getPostById(req.params.id)
    if (!post) {
        return res.status(404).json({ errors: ['Post not found'] })
    }
    if (post.postOwnerID != req.userId) {
        return res.status(401).json({ errors: ['Unauthorized'] })
    }

    const updatedPost = await postService.updatePost(post, req.body.content)
    if (!updatedPost) {
        return res.status(500).json({ errors: ['Internal server error'] })
    }
    res.json(updatedPost)
}

const likePost = async (req, res) => {
    const post = await postService.likePost(req.params.id, req.userId);
    if (!post) {
        return res.status(404).json({ errors: ['Post not found'] });
    }
    res.json(post.likesID.length);
}

const checkIfAuth = async (req, res) => {
    const post = await postService.getPostById(req.params.id)
    if (!post) {
        return res.status(404).json({ errors: ['Post not found'] })
    }
    if (post.postOwnerID != req.userId) {
        return res.status(401).json({ errors: ['Unauthorized'] })
    }
    res.json(post)
}

const deletePost = async (req, res) => {
    try {
        const post = await postService.getPostById(req.params.id);
        if (!post) {
            return res.status(404).json({ errors: ['Post not found'] });
        }

        if (post.postOwnerID != req.userId) {
            return res.status(401).json({ errors: ['Unauthorized'] });
        }
        await postService.deletePost(post);
        return res.json({ message: 'Post deleted successfully' });
    } catch (error) {
        console.error('Failed to delete post:', error);
        return res.status(500).json({ errors: ['Internal server error'] });
    }
}
const getFriendsPosts = async (req, res) => {
    const posts = await postService.getFriendsPosts(req.userId);
    res.json(posts);
}



const addComment = async (req, res) => {
    try {
        const post = await postService.getPostById(req.params.id);
        if (!post) {
            return res.status(404).json({ errors: ['Post not found'] });
        }

        const commentContent = req.body.content;
        if (!commentContent) {
            return res.status(400).json({ errors: ['Comment content is required'] });
        }

        const newComments = await postService.addComment(req.userId, commentContent, post);
        // Return the comments of the updated post
        return res.json(newComments);
    } catch (error) {
        console.error('Failed to add comment:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

const deleteComment = async (req, res) => {
    try {
        const postid = req.params.postid;
        const commentid = req.params.commentid;

        const result = await postService.deleteComment(postid, commentid, req.userId);
        if (result.error) {
            return res.status(result.status).json({ errors: [result.error] });
        }
        res.json(result.post.comments);
    } catch (error) {
        console.error('Failed to delete comment:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

const likeComment = async (req, res) => {
    const commentId = req.params.commentid;
    const postId = req.params.postid;

    const commentLikes = await postService.likeComment(postId, commentId, req.userId);
    if (!commentLikes) {
        return res.status(404).json({ errors: ['Post not found'] });
    }
    res.json(commentLikes);
}
const updateComment = async (req, res) => {
    const post = await postService.getPostById(req.params.postid);
    const commentId = req.params.commentid;
    if (!post) {
        return res.status(404).json({ errors: ['Post not found'] });
    }
    const updateComment = await postService.updateComment(commentId, post, req.body.content);
    if (!updateComment) {
        return res.status(500).json({ errors: ['Internal server error'] })
    }
    res.json(updateComment)
}
const checkIfAuthComment = async (req, res) => {
    const post = await postService.getPostById(req.params.postid)
    if (!post) {
        return res.status(404).json({ errors: ['Post not found'] })
    }
    if (req.params.commentname != req.userId) {
        return res.status(401).json({ errors: ['Unauthorized'] })
    }
    res.json(true);
}




module.exports = { createPost, updatePost, getPosts, getPost, deletePost, likePost, updateImage,
    addComment, updateComment,deleteComment, checkIfAuth, checkIfAuthComment, likeComment, getFriendsPosts}