
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

const compTime = (a, b) => { // 时间a是否早于时间b
    let aLit = a.split('-')
    let bLit = b.split('-')
    if (aLit[0] !== bLit[0])
        return Number(aLit[0]) > Number(bLit[0])
    else if (aLit[1] !== bLit[1])
        return Number(aLit[1]) > Number(bLit[1])
    return Number(aLit[2]) > Number(bLit[2])
}

const inDateRange = (now, start, end) => { // 判断是否在时间范围内
    // console.log('inDate', now, start, end)
    if (start == null && end == null)
        return true
    if (start != null && end != null)
        return (compTime(end, now) || end == now) && (compTime(now, start) || start == now)
    if (start != null && end == null)
        return compTime(now, start) || start == now
    if (start == null && end != null)
        return compTime(end, now) || end == now
}

export {
    sleep,
    timeFormat,
    compTime,
    inDateRange
}