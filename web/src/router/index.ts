import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/home.vue'
import AdminUser from '../views/admin/admin-user.vue'
import store from '@/store';
import {Tool} from "@/util/tool"

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/about.vue')
  },
	{
	  path: '/admin/ebook',
	  name: 'AdminEbook',
	  // route level code-splitting
	  // this generates a separate chunk (about.[hash].js) for this route
	  // which is lazy-loaded when the route is visited.
	  component: () => import(/* webpackChunkName: "AdminEbook" */ '../views/admin/admin-ebook.vue'),
		meta: {
			loginRequire : true
		}
	},
	{
	  path: '/admin/category',
	  name: 'AdminCategory',
	  // route level code-splitting
	  // this generates a separate chunk (about.[hash].js) for this route
	  // which is lazy-loaded when the route is visited.
	  component: () => import(/* webpackChunkName: "AdminCategory" */ '../views/admin/admin-category.vue'),
		meta: {
			loginRequire: true
		}
	},
	{
	  path: '/admin/doc',
	  name: 'AdminDoc',
	  // route level code-splitting
	  // this generates a separate chunk (about.[hash].js) for this route
	  // which is lazy-loaded when the route is visited.
	  component: () => import(/* webpackChunkName: "AdminDoc" */ '../views/admin/admin-doc.vue'),
		meta: {
			loginRequire: true
		}
	},
	{
	  path: '/doc',
	  name: 'Doc',
	  // route level code-splitting
	  // this generates a separate chunk (about.[hash].js) for this route
	  // which is lazy-loaded when the route is visited.
	  component: () => import(/* webpackChunkName: "AdminDoc" */ '../views/doc.vue')
	},
	{
	  path: '/admin/user',
	  name: 'AdminUser',
	  component: AdminUser,
		meta: {
			loginRequire: true
		}
	},
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire;
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
