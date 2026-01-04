async function fetchPosts(){
    const response = await fetch("http://localhost:8080/Post")
    const posts = await response.json();
    console.log(posts)
}

fetchPosts()