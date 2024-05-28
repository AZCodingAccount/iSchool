import { deepClone } from '@/utils/tool'

// 归并排序
const orderRecursion = (lit, keyFn = (item) => { return item }, compareFn = (a, b) => { return a > b }, l, r) => {
    if (l >= r)
        return
    else {
        var mid = parseInt((l + r) / 2)
        orderRecursion(lit, keyFn, compareFn, l, mid)
        orderRecursion(lit, keyFn, compareFn, mid + 1, r)
    }

    let stepL = l
    let stepR = mid + 1
    let tempLit = []
    while (stepL <= mid && stepR <= r) {
        if (compareFn(keyFn(lit[stepL]), keyFn(lit[stepR])))
            tempLit.push(lit[stepL++])
        else
            tempLit.push(lit[stepR++])
    }
    while (stepL <= mid)
        tempLit.push(lit[stepL++])
    while (stepR <= r)
        tempLit.push(lit[stepR++])

    for (let i = l; i <= r; i++)
        lit[i] = tempLit[i - l]
}

// 归并排序递归入口
const orderArray = (lit, keyFn = (item) => { return item }, compareFn = (a, b) => { return a > b }, reverse = false) => {
    let lit1 = deepClone(lit)
    orderRecursion(lit1, keyFn, compareFn, 0, lit.length - 1)
    if (reverse)
        return lit1.reverse()
    return lit1
}

export {
    orderArray
}




