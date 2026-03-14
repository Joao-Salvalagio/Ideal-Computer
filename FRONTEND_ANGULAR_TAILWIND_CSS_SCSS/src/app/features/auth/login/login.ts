import { Component, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { LoginRequest } from '../../../shared/models/auth.model';
import { AuthService } from '../../../core/services/auth';

// Componente autônomo responsável por renderizar e gerenciar a tela de login.
// Resolve o problema de captura de credenciais do usuário, aplicando validações reativas
// antes de despachar a requisição de autenticação para o serviço correspondente.
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  // Define o formulário reativo com seus respectivos campos e regras de validação.
  // Utiliza o Validators nativo do Angular para evitar requisições desnecessárias à API
  // caso o formato de e-mail seja inválido ou os campos estejam em branco.
  public loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
  });

  // Valida e submete as credenciais do usuário para a API Backend.
  // Em caso de sucesso, armazena o token de sessão localmente (TODO) e redireciona
  // o usuário para o painel principal do sistema.
  public onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const credentials: LoginRequest = {
      email: this.loginForm.value.email!,
      password: this.loginForm.value.password!,
    };

    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Token recebido do Java:', response.token);
        // Futuramente salvaremos o token no LocalStorage aqui
        // this.router.navigate(['/admin']);
      },
      error: (err) => {
        console.error('Erro de autenticação:', err);
        alert('Credenciais inválidas. Tente novamente.');
      },
    });
  }
}
