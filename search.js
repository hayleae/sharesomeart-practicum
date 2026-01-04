const searchForm = document.querySelector(".input-group");
const searchResultContainer = document.querySelector(".search-results");

// Notification
const notyf = new Notyf({
  duration: 2000,
  position: {
    x: "right",
    y: "top",
  },
});

searchForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const searchType = searchForm.searchType.value;
  const keyword = searchForm.searchInput.value;

  console.log(searchType);

  if (!keyword) {
    notyf.error("There is nothing to search");
    return;
  }

  const searchRequest = {
    keyword,
  };

  console.log(JSON.stringify(searchRequest));

  // Make different post requests depending on the search type

  try {
    // By username
    if (searchType === "user") {
      const res = await fetch("http://localhost:8080/Search/user/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(searchRequest),
      });

      const data = await res.json();

      if (!res.ok) {
        notyf.error(data.message);
      }

      let searchResult = "";

      data.map((user) => {
        searchResult += `<div>
        <a href="/user-public.html?id=${user.id}" class="ms-5 text-capitalize">${user.username}</a>
        </div>`;
      });

      searchResultContainer.innerHTML = searchResult;
    }
    // By post title
    else if (searchType === "title") {
      const res = await fetch("http://localhost:8080/Search/title/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(searchRequest),
      });

      const data = await res.json();

      if (!res.ok) {
        notyf.error(data.message);
      }

      let searchResult = "";

      data.map((post) => {
        console.log(post);
        searchResult += `<div>
        
              <a href="/post.html?id=${post.id}" class="ms-5 text-capitalize">${post.title}</a> 
        
        </div>`;
      });

      searchResultContainer.innerHTML = searchResult;
    } else {
      // By post tag
      const res = await fetch("http://localhost:8080/Search/tag/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(searchRequest),
      });

      const data = await res.json();

      if (!res.ok) {
        notyf.error(data.message);
      }

      let searchResult = "";

      data.map((post) => {
        console.log(post);
        searchResult += `<div>
        
             <a href="/post.html?id=${post.id}" class="ms-5 text-capitalize">${post.title}</a> 
        
        </div>`;
      });

      searchResultContainer.innerHTML = searchResult;
    }
  } catch (error) {
    console.log(error);
  }
});
