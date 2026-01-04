/*
    The following code fetches a post by its ID from the server and populates the HTML elements with the post's details.
    It waits for the DOM content to be fully loaded before executing.

    Author: Theodore Kaltsas
*/

/*
window.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get('id');

    fetch(`/Post/postId/{id}`)
        .then(res => res.json())
        .then(post => {
            var image = new Image();
            image.src = "data:image/png;base64," + post.image;
            console.log(image);
            document.getElementById('image').setAttribute('src', "data:image/png;base64," + post.image);
            //document.getElementById('image').textContent = convertToImage(post.image);
            document.getElementById('title').textContent = post.title;
            document.getElementById('description').textContent = post.description;
            document.getElementById('tag').textContent = post.tag;
        });
});
*/
const postTitle = document.getElementById("title");
const postDescription = document.getElementById("description");
const postTag = document.getElementById("tag");
const postImg = document.getElementById("image");
const postAuthor = document.getElementById("author");

const commentTextArea = document.getElementById("comment-area");
const commentsContainer = document.getElementById("comments");
let comments = [];

const editArea = document.getElementById("edit-area");
const deleteArea = document.getElementById("delete-area");

let commentData = {
  userId: null,
  postId: null,
  message: null,
  author: null,
  createdAt: null,
};

// Get current user id
let currentUserId = null;
let postUser = null;
let postId = null;

const fetchSinglePost = async () => {
  const params = new URLSearchParams(window.location.search);

  postId = params.get("id");

  const res = await fetch(`/Post/postId/${postId}`);

  const data = await res.json();

  postImg.src = "data:image/png;base64," + data.image;
  postTitle.textContent = data.title;
  postDescription.textContent = data.description;
  postTag.textContent = data.tag;

  postUser = data.userId;
  postAuthor.innerHTML = `<a href="/user-public.html?id=${postUser}">${data.author}</a>`;

  // data for post comments
  commentData.postId = data.id;
};

async function fetchUser() {
  try {
    const response = await fetch("http://localhost:8080/currentUser");
    const data = await response.json();

    console.log(data);

    commentData.userId = parseInt(data.id);
    commentData.author = data.username;

    // set current user id
    currentUserId = parseInt(data.id);

    //delete/edit function check
    if (currentUserId == postUser){
      //Call edit and delete functions
      renderEditandDelete();
    }

    // Render comments
    renderComments();
  } catch (error) {
    console.error("Error fetching user:", error);
  }
}

commentTextArea.addEventListener("keydown", async (e) => {
  if (e.key === "Enter") {
    e.preventDefault();

    const message = commentTextArea.value.trim();

    const newComment = {
      userId: commentData.userId,
      postId: commentData.postId,
      message: message,
      author: commentData.author,
    };

    comments.unshift(newComment);

    renderComments();

    try {
      const res = await fetch("http://localhost:8080/post/addComment", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newComment),
      });

      const data = await res.json();

    } catch (error) {}
    e.target.value = "";
  }
});

const renderEditandDelete = () =>{
    editArea.innerHTML = `<a href="/edit-post.html?id=${postId}" class="btn btn-success mt-2">Edit</a>`;
    deleteArea.innerHTML = `<button class="btn btn-danger w-30" onClick="deletePost(${postId})">Delete</button>`;
}

const renderComments = () => {
  commentsContainer.innerHTML = comments
    .map((comment) => {
      const color = getAuthorColor(comment.author);

      // Format date
      const formattedDate = comment.createdAt
        ? new Intl.DateTimeFormat("en-US").format(
            new Date(comment.createdAt.split("-").join(", "))
          )
        : new Intl.DateTimeFormat("en-US").format(new Date());

      return `<div class="mb-2 p-2 bg-white border rounded shadow-sm">
                <div class="d-flex justify-content-between align-items-center px-2 mt-2">
                <strong class="text-capitalize badge bg-${color}">${
        comment.author
      }</strong>
                <small>${formattedDate}</small>
                </div>
                <div class="comment-body px-2 mt-3">
                <div class="d-flex justify-content-between align-items-center>
                <p style="width: 30ch;">${comment.message}</p>
                ${
                  currentUserId === comment.userId
                    ? `<svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="16"
                      height="16"
                      fill="currentColor"
                      class="bi bi-trash text-danger"
                      viewBox="0 0 16 16"
                      style="cursor: pointer;"
                      onClick="deleteComment(${comment.id})"
                    >
                      <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z" />
                      <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z" />
                    </svg>`
                    : ""
                }
                </div>
                </div>
            </div>`;
    })
    .join("");
};

//   Load comments
const fetchPostComments = async () => {
  const res = await fetch(
    `http://localhost:8080/posts/${commentData.postId}/comments`
  );

  comments = await res.json();

  renderComments();
};

// Dynamically set a badge for an author
const badgeColors = [
  "primary",
  "secondary",
  "success",
  "dark",
  "danger",
  "warning",
];
const authorColors = new Map();
let colorIdx = 0;

function getAuthorColor(author) {
  if (!authorColors.has(author)) {
    authorColors.set(author, badgeColors[colorIdx % badgeColors.length]);
    colorIdx++;
  }

  return authorColors.get(author);
}

const deleteComment = async (id) => {
  try {
    comments = comments.filter((c) => c.id !== parseInt(id));
    renderComments();

    const res = await fetch(`http://localhost:8080/post/comment/${id}`, {
      method: "DELETE",
    });

    const data = await res.json();

  } catch (error) {}
};

const deletePost = async (id) => {
  try {
    const res = await fetch(`http://localhost:8080/Post/delete?id=${id}`);
    const data = await res.json();

    console.log(data);

    window.location.href = "/user.html";
  } catch (error){

  }
}

window.addEventListener("DOMContentLoaded", async () => {
  await fetchSinglePost();
  await fetchPostComments();
  await fetchUser();
});
