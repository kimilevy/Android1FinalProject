const items = require('./recipes.js')
const fs = require('fs');

query = `INSERT INTO 'recipe' (result, first_ingredient, second_ingredient) VALUES`
const keys = Object.keys(items)
for (let recipeKey in items) {
    for (let [first, second] of items[recipeKey].recipes) {
        if (!second || !first) {
            continue;
        }
        first = first.toLowerCase()
        second = second.toLowerCase()
        recipeKey = recipeKey.toLowerCase()
        if (!keys.includes(first) || !keys.includes(second))
            continue;

        query += ` ('${items[recipeKey].id}', '${first}', '${second}')`
        if (recipeKey === 'zombie' && first === 'human' && second === 'zombie') {
            break;
        }
        query += ',\n'
    }
}
query += ';'


fs.writeFileSync('./query.txt', query)