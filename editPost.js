const editPostForm = document.querySelector(".edit-post-form");
const submitBtn = document.getElementById("submit-btn");
const spinner = document.getElementById("spinner");
const btnTxt = document.getElementById("btn-txt");

// Notification
const notyf = new Notyf({
  duration: 2000,
  position: {
    x: "right",
    y: "top",
  },
});

const params = new URLSearchParams(window.location.search);
const postId = params.get("id");

const getPost = async (id) =>{

  try{
    const res = await fetch(`http://localhost:8080/Post/postId/${id}`);
    const data = await res.json();
    console.log(data);

    editPostForm.title.value = data.title;
    editPostForm.description.value = data.description;
    editPostForm.tag.value = data.tag;

  } catch (error){

    console.log(error);
  }
}

editPostForm.addEventListener("submit", async function(e){
  e.preventDefault();

  spinner.classList.remove("d-none");
  btnTxt.textContent = "Submitting...";
  submitBtn.disabled = true;

  const title = editPostForm.title.value;
  const description = editPostForm.description.value;
  const tag = editPostForm.tag.value;
  const image = editPostForm.imageFile.files[0];

  const postData = new FormData();

  postData.append("postId", postId);

  //Optional fields because editing an existing post
  title ? postData.append("title", title) : null;
  description ? postData.append("description", description) : null;
  tag ? postData.append("tag", tag) : null;
  image ? postData.append("image", image) : null;

  // Check that at least one field is being updated
  if (!title && !description && !tag && !image) {
    notyf.error("At least one change is required");
    spinner.classList.add("d-none");
    btnTxt.textContent = "Submit";
    submitBtn.disabled = false;
    return;
  }
  try {
    const res = await fetch("http://localhost:8080/save-post", {
      method: "POST",
      body: postData,
    });

    const data = await res.json();

    console.log(data);

    if (!res.ok) {
      notyf.error(data.message);

      return;
    }

    // Any redirect needed goes here
    window.location.href = "/user.html";
  } catch (error) {
    console.log(error);
  } finally {
    spinner.classList.add("d-none");
    btnTxt.textContent = "Submit";
    submitBtn.disabled = false;
  }
});

window.addEventListener("DOMContentLoaded", async () => {
  getPost(postId);
});