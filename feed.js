var followedUsers = [];
var posts = [];
const userPostsContainer = document.getElementById("user-posts");

const fetchUser = async() => {

try {
        const response = await fetch("http://localhost:8080/currentUser");
        const user = await response.json();

        const userId = user.id;
        await getFollowedAccounts(userId);

    } catch (error){
        console.log("Error fetching User: ", error);
    }

}

async function getFollowedAccounts(userId){

    try {
        const followerResponse = await fetch(`http://localhost:8080/followed/all?userId=${userId}`);
        const followed = await followerResponse.json();

        for (var i = 0; i < followed.length; i++){
            followedUsers.push(followed[i].followedId);
            await getUsersPosts(followed);
        }
        //await getUsersPosts(followed);

    } catch (error){
        console.log("Error Fetching Followed Accounts" , error);
    }
}

async function getUsersPosts(users){

    //console.log(users);

    var ids = [];
    for (var i = 0; i < users.length; i++){
        ids.push(users[i].followedId);
    }


    try {
        const postResponse = await fetch(`http://localhost:8080/Post/users?ids=${ids}`);
        //console.log(postResponse);

        posts = await postResponse.json();
        //console.log(posts);
        let singlePost = "";
 
        posts.map((post) => {
            const img = "data:image/png;base64," + post.image;
 
            singlePost += `
  <div class="col-12">
<a href="/post.html?id=${post.id}" class="text-primary"  style="display: inline-block; padding-left: 26rem;">
<div class="card my-2" style="width: 40rem; height: 25rem;">
<img src="${img}" class="card-img-top h-100 w-100" style="object-fit: cover" alt="${post.title}">
</div>
</a>
</div>`;
    });
 
    userPostsContainer.innerHTML = singlePost;
        //create for-loop to set them all as PNG  -> "data:image/png:base64," before sending to frontend. 

    } catch (error){

        console.log("Error Fetching Posts", error);

    }

}

window.addEventListener('DOMContentLoaded', async() => {
    await fetchUser();
});
