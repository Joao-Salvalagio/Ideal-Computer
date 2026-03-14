import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';
import { RegisterComponent } from './features/auth/register/register';

export const routes: Routes = [
  // Quando o usuário entrar no site vazio, joga ele pro login
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Nossas rotas de autenticação
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
];
