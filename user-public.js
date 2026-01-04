const followBtn = document.getElementById("follow-btn");
const usernameContainer = document.querySelector(".username");
const postHeader = document.getElementById("posts-header");
const followContainer = document.querySelector(".follower-following");
const userPostsContainer = document.getElementById("user-posts");

let currentUser = {
  id: null,
  username: null,
  email: null,
};

let userPublic = {
  id: null,
  username: null,
  email: null,
  followers: 0,
  following: 0,
};

async function getUserPublic() {
  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");

  try {
    const res = await fetch(`http://localhost:8080/User/${id}`);

    const data = await res.json();

    console.log(data);

    userPublic.id = parseInt(data.id);
    userPublic.username = data.username;
    userPublic.email = data.email;
    userPublic.followers = parseInt(data.followers);
    userPublic.following = parseInt(data.following);

    await fetchUserPosts(userPublic.id);
  } catch (error) {
    console.log(error);
  }
}

followBtn.addEventListener("click", toggleFollow);

async function toggleFollow() {
  if (!userPublic || !userPublic.id) return;

  const action = followBtn.innerText.toLowerCase();

  if (action === "follow") {
    userPublic.followers++;
    followBtn.classList.remove("btn-success");
    followBtn.classList.add("btn-danger");
    followBtn.innerText = "Unfollow";
  } else {
    userPublic.followers--;
    followBtn.classList.remove("btn-danger");
    followBtn.classList.add("btn-success");
    followBtn.innerText = "Follow";
  }

  followContainer.textContent = `Followers: ${userPublic.followers} | Following: ${userPublic.following}`;

  const url = action === "follow" ? "/follow" : "/unfollow";
  try {
    const response = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ followedId: userPublic.id }),
    });
    if (!response.ok) {
      console.log("Something went wrong...");
    }
  } catch (err) {
    console.log(err);
  }
}

async function checkFollowStatus() {
  if (!userPublic || !userPublic.id) return;

  if (userPublic.id == currentUser.id) {
    followBtn.style.display = "none";
  }

  try {
    const response = await fetch(`/api/is-following/${userPublic.username}`);
    const data = await response.json();

    if (data.isFollowing) {
      followBtn.innerText = "Unfollow";
      followBtn.classList.remove("btn-success");
      followBtn.classList.add("btn-danger");
    } else {
      followBtn.innerText = "Follow";
      followBtn.classList.remove("btn-danger");
      followBtn.classList.add("btn-success");
    }
  } catch (err) {
    console.error("Error checking follow status:", err);
  }
}

const getCurrentUser = async () => {
  try {
    const res = await fetch("http://localhost:8080/currentUser");

    const data = await res.json();

    currentUser.id = data.id;
  } catch (error) {
    console.log(error);
  }
};

async function fetchUserPosts(userId) {
  try {
    const response = await fetch(`http://localhost:8080/Post/user/${userId}`);
    const posts = await response.json();

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

window.addEventListener("DOMContentLoaded", async () => {
  await getUserPublic();
  await getCurrentUser();
  await checkFollowStatus();

  usernameContainer.textContent = userPublic.username;
  postHeader.textContent = `${userPublic.username}'s Posts`;
  followContainer.textContent = `Followers: ${userPublic.followers} | Following: ${userPublic.following}`;
});
