
// 深拷贝
const deepClone = (obj) => {
    if (typeof obj !== 'object')
        return obj
    let objClone = Array.isArray(obj) ? [] : {}
    let length = Array.isArray(obj) ? obj.length : Object.getOwnPropertyNames(obj).length
    if (length !== 0) {
        for (let key in obj) {
            if (Array.isArray(obj))
                objClone.push(deepClone(obj[key]))
            else
                objClone[key] = deepClone(obj[key])
        }
    }
    return objClone
}

export {
    deepClone
}
