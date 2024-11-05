const fileService = (() => {
    const upload = async (formDate) => {
        await fetch("/members/file/upload", {
            method: "post",
            body: formDate
        });
    }

    return {upload: upload};
})
