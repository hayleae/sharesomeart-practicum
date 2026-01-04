const loginForm = document.querySelector(".login-form");
const cancelBtn = document.querySelector(".cancelbutton");

// Notification
const notyf = new Notyf({
  duration: 2000,
  position: {
    x: "right",
    y: "top",
  },
});

loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const userNameOrEmail = loginForm.usernameOrEmail.value;
  const password = loginForm.password.value;

  const loginData = {
    userNameOrEmail,
    password,
  };

  // Check required fields are provided
  if (!userNameOrEmail || !password) {
    notyf.error("All fields required");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
      credentials: "include",
    });

    const data = await res.json();

    console.log(data.message);

    if (!res.ok) {
      notyf.error(data.message);

      return;
    }

    notyf.success(data.message);
    window.location.href = "/feed.html";
  } catch (error) {
    console.log(error);
  }
});

cancelBtn.addEventListener("click", () => {
  loginForm.usernameOrEmail.value = "";
  loginForm.password.value = "";
});
