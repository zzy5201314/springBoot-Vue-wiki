module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/typescript/recommended'
  ],
  parserOptions: {
    ecmaVersion: 2020
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
		// 'generator-star-spacing': 'off',//禁止空格报错检查
		'no-mixed-spaces-and-tabs': 'off',  //去除空格检查
		'no-unused-components':	'off',
		'@typescript-eslint/no-explicit-any':	'off',
		'vue/no-unused-vars': 'off',
		'@typescript-eslint/no-unused-vars': 'off',
		'@typescript-eslint/ban-types': 'off',
		'eslint-disable-next-line': 'off',
		'eslint-disable': 'off',
		'@typescript-eslint/explicit-module-boundary-type': 'off'
  }
}
