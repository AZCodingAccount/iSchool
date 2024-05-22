
// const sleep = (time) => {
//     var timeStamp = new Date().getTime();
//     var endTime = timeStamp + time;
//     while (true) if (new Date().getTime() > endTime) return;
// }


const sleep = (time) => {
    // a()
    return new Promise((resolve) => setTimeout(resolve, time))
}

const timeFormat = (time) => {
    return time.split('T')[0]
}

export {
    sleep,
    timeFormat
}