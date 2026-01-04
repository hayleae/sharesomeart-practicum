const usernameContainer = document.querySelector(".username");
const postHeader = document.getElementById("posts-header");
const followContainer = document.querySelector(".follower-following");
const userPostsContainer = document.getElementById("user-posts");

let currentUser = {
  id: null,
  username: null,
  email: null,
  followers: 0,
  following: 0,
};

async function fetchUser() {
  try {
    const response = await fetch("http://localhost:8080/currentUser");
    const data = await response.json();

    currentUser.id = parseInt(data.id);
    currentUser.username = data.username;
    currentUser.email = data.email;
    currentUser.followers = parseInt(data.followers);
    currentUser.following = parseInt(data.following);

    console.log(currentUser);

    await fetchUserPosts(currentUser.id);
  } catch (error) {
    console.error("Error fetching user:", error);
  }
}

async function fetchUserPosts(userId) {
  try {
    const response = await fetch(`http://localhost:8080/Post/user/${userId}`);
    const posts = await response.json();

    console.log(posts);

    let singlePost = "";

posts.map((post) => {
            const img = "data:image/png;base64," + post.image;
 
            singlePost += `
  <div class="col-12">
<a href="/post.html?id=${post.id}" class="text-primary"  style="display: inline-block; padding-left: 15rem;">
<div class="card my-2" style="width: 40rem; height: 25rem;">
<img src="${img}" class="card-img-top h-100 w-100" style="object-fit: cover" alt="${post.title}">
</div>
</a>
</div>`;
    });

    userPostsContainer.innerHTML = singlePost;
  } catch (error) {
    console.log(error);
  }
}
async function logout() {
  try {
    const response = await fetch("http://localhost:8080/logout", {
      method: "POST",
      credentials: "include",
    });

    const data = await response.json();

    console.log(data.message);

    if (!response.ok) {
      return;
    }

    window.location.href = "/login.html";
  } catch (error) {
    console.error("There was an error logging out:", error);
  }
}

document.getElementById("logoutButton").addEventListener("click", logout);

window.addEventListener("DOMContentLoaded", async () => {
  await fetchUser();

  usernameContainer.textContent = currentUser.username;
  postHeader.textContent = `${currentUser.username}'s Posts`;
  followContainer.textContent = `Followers: ${currentUser.followers} | Following: ${currentUser.following}`;
});
