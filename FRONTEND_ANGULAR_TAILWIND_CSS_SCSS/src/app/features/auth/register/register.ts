import { Component, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RegisterRequest } from '../../../shared/models/auth.model';
import { AuthService } from '../../../core/services/auth';

// Componente autônomo responsável por renderizar e gerenciar a tela de cadastro.
// Resolve o problema de criação de novas contas, aplicando validações reativas rigorosas
// no lado do cliente antes de despachar a requisição para o serviço de autenticação,
// poupando processamento desnecessário na API Backend.
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  // Define o formulário reativo estrito com Nome, E-mail e Senha.
  // Utiliza validadores nativos para garantir que os dados submetidos
  // respeitem as regras de negócio da API (ex: senha com mínimo de 6 caracteres).
  public registerForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
  });

  // Valida e submete os dados do novo usuário para a API Backend.
  // Em caso de sucesso, exibe um alerta de confirmação e redireciona
  // o usuário de volta para a tela de login.
  public onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const requestData: RegisterRequest = {
      name: this.registerForm.value.name!,
      email: this.registerForm.value.email!,
      password: this.registerForm.value.password!,
    };

    this.authService.register(requestData).subscribe({
      next: (response) => {
        console.log('Resposta do Java:', response);
        alert('Conta criada com sucesso! Faça login para continuar.');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Erro ao registrar:', err);
        alert('Erro ao criar conta. Verifique os dados e tente novamente.');
      },
    });
  }
}
