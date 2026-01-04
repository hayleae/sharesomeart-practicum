const createPostForm = document.querySelector(".create-post-form");
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

createPostForm.addEventListener("submit", async function (e) {
  e.preventDefault();

  spinner.classList.remove("d-none");
  btnTxt.textContent = "Submitting...";
  submitBtn.disabled = true;

  const title = createPostForm.title.value;
  const description = createPostForm.description.value;
  const tag = createPostForm.tag.value;
  const image = createPostForm.imageFile.files[0];

  const postData = new FormData();

  postData.append("title", title);
  postData.append("description", description);
  postData.append("tag", tag);
  postData.append("image", image);

  //   Check required fields are provided
  if (!title || !description || !tag || !image) {
    notyf.error("All fields required");
    spinner.classList.add("d-none");
    btnTxt.textContent = "Submit";
    submitBtn.disabled = false;
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/create-post", {
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
