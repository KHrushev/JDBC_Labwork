// const form = document.getElementById("orderForm");
// const btn = document.getElementById("submitOrderBtn");
//
// const products = sessionStorage.getItem("products");
// console.log("ASDSAD");
//
// btn.addEventListener('click', function (event) {
//     event.preventDefault();
//     const check = form.checkValidity();
//     form.reportValidity();
//
//     if (check) {
//         const formData = new FormData(form);
//         formData.append('products', JSON.stringify(products));
//
//         fetch('/confirm', {
//             method: 'POST',
//             body: formData
//         }).then(function (response) {
//             return response.text();
//         }).then(function (text) {
//             document.write(text);
//         });
//     }
// });

function sendForm(event) {
    event.preventDefault();
    
    const form = document.getElementById("orderForm");
    const products = sessionStorage.getItem("products");

    console.log(products);
    const check = form.checkValidity();
    form.reportValidity();

    if (check) {
        const formData = new FormData(form);
        formData.append('products', JSON.stringify(products));

        fetch('/confirm', {
            method: 'POST',
            body: formData
        }).then(function (response) {
            return response.text();
        }).then(function (text) {
            document.write(text);
        });
    }
}
