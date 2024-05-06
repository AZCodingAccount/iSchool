
// const sleep = (time) => {
//     var timeStamp = new Date().getTime();
//     var endTime = timeStamp + time;
//     while (true) if (new Date().getTime() > endTime) return;
// }


function sleep(time) {
    // a()
    return new Promise((resolve) => setTimeout(resolve, time))
}

export { sleep }