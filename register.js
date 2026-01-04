const registerForm = document.querySelector(".register-form");

// Notification
const notyf = new Notyf({
  duration: 2000,
  position: {
    x: "right",
    y: "top",
  },
});

registerForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = registerForm.username.value;
  const email = registerForm.email.value;
  const password = registerForm.password.value;
  const confirmPassword = registerForm.confirmPassword.value;

  const userData = {
    username,
    email,
    password,
    confirmPassword,
  };

  // Check required fields are provided
  if (!username || !password || !confirmPassword) {
    console.log("All fields required");

    notyf.error("All fields required");
    return;
  }

  // Verify email is valid
  if (!validateEmail(email)) {
    notyf.error("Provide a valid email");
  }

  // Verify passwords match
  if (password !== confirmPassword) {
    notyf.error("Password do not match");
    return;
  }

  console.log(userData);

  try {
    const res = await fetch("http://localhost:8080/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userData),
    });

    const data = await res.json();

    console.log(data.message);

    if (!res.ok) {
      notyf.error(data.message);

      return;
    }

    window.location.href = "/login.html";
  } catch (error) {
    console.log(error);
  }
});

function validateEmail(emailId) {
  const mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

  return emailId.match(mailformat);
}
