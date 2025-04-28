import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

import Register from '../views/Register.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import RegisterConfirm from '../views/RegistrationConfirmation.vue'
import AdminMain from '../views/admin/AdminMainScreen.vue'
import ManagePromotions from '../views/admin/ManagePrmotions.vue'
import ManageUsers from '../views/admin/ManageUser.vue'
import ManageMovies from '../views/admin/ManageMovies.vue'
import MovieDetailView from '../views/MovieDetailView.vue';
import CheckOut from '../views/CheckOut.vue';
import OrderConfirmation from '../views/OrderConfirmation.vue';
import TicketPage from '../views/TicketPage.vue';
import EditProfile from '../views/EditProfile.vue';
import Cart from '../views/Cart.vue';
import  UserMainScreen  from '../views/UserMainScreen.vue';
import ConfirmSuccess from '../views/EmailConfirmation.vue';
import ForgetPassword from '../views/ForgetPwd.vue';
import ResetPwd from '../views/ResetPwd.vue';


const isAuthenticated = () => localStorage.getItem('token') !== null;
const isAdmin = () => localStorage.getItem('role') === 'ADMIN';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    { path: '/movie/:id', name: 'movie-detail', component: MovieDetailView,},
    ,
    {
      path : '/usermainscreen',
      name :'usermainscreen',
      component: UserMainScreen,
    }
    ,
    {
      path:'/editprofile',name:'editprofile',component: EditProfile
    },
    {

      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    // registration 
    {
        path: '/register',
        name: 'register',
        component: Register
    },
    // login
    {
        path: '/login',
        name: 'login',
        component: Login,

    },
    // logout 
    {
      path: '/logout',
      name: 'logout',
      component: Logout
    },


    // registration confirmation
    {
        path: '/register/confirm/:username', // 定义带有 :username 参数的路径
        name: 'register-confirm',
        component: RegisterConfirm
    },
    {
        path: '/register/confirm/success',
        name: 'ConfirmSuccess',
        component: ConfirmSuccess,  // 你的成功页面组件
    },
      
    // admin main screen
    {
        path: '/admin',
        name: 'admin',
        component: AdminMain,
        beforeEnter: (to, from, next) => {
            if (!isAuthenticated()) {
              // 未登录，跳转到登录页面
              return next({ path: '/login' });
            }
            if (!isAdmin()) {
              // 没有管理员权限，跳转到错误页面或其他地方
              return next({ path: '/unauthorized' });
            }
            next(); // 用户已登录且为管理员，允许访问
          },
    },
    {
        path: '/admin/promotions',
        name: 'admin-promotions',
        component: ManagePromotions
    },
    // manage users
    {
        path: '/admin/users',
        name: 'admin-users',
        component: ManageUsers
    },
    {
        path: '/admin/movies',
        name: 'admin-movies',
        component: ManageMovies
    },

    {
      path: '/OrderConfirmation',
      name: 'OrderConfirmation',
      component: OrderConfirmation,
    },
    {
      path:'/TicketPage',
      name:'TicketPage',
      component: TicketPage,
    },
    {
      path:'/CheckOut',
      name:'CheckOut',
      component: CheckOut,
    }
    ,
    {
      path: '/cart',
      name: 'Cart',
      component: Cart
    },
    {
      path: '/ForgetPwd',
      name: 'ForgetPwd',
      component: ForgetPassword
    },
    {
      path: '/ResetPwd',
      name: 'ResetPwd',
      component: ResetPwd
    },

    {
        path: '/unauthorized',
        component: () => import('../views/UnauthorizedScreen.vue'), 
      },
    {
        path: '/OrderHistory',
        name: 'OrderHistory',
        component: () => import('../views/OrderHistory.vue'),
    }
  ]
})


export default router;
