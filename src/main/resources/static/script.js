const DEFAULT_IMAGE = "img/img.png";

const preview = document.getElementById("myImage");
const fileInput = document.getElementById("profileFile");
const btnDelete = document.getElementById("btnDeleteImage");
const btnEdit = document.getElementById("btnEditImage");


document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("editForm").addEventListener("submit", e => {
        e.preventDefault();
        const fd = new FormData(e.target);

        fetch("/user", {
            method: "PATCH",
            body: fd,
        })
            .then(async (res) => {
                if (!res.ok) {
                    const html = await res.text();
                    document.open();
                    document.write(html);
                    document.close();
                    return;
                }
                location.href = "/";
            })
            .catch(console.error);
    });

    btnDelete.addEventListener("click", (e) => {
        e.preventDefault();
        preview.src = DEFAULT_IMAGE;
    });

    btnEdit.addEventListener("click", (e) => {
        e.preventDefault();
        fileInput.click();
    });

    fileInput.addEventListener("change", () => {
        const file = fileInput.files[0];
        if (!file) return;

        if (!file.type.startsWith("image/")) {
            alert("이미지 파일만 선택할 수 있습니다.");
            fileInput.value = "";
            return;
        }

        preview.src = URL.createObjectURL(file);
    });
});
