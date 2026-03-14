import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { LoginRequest, AuthResponse, RegisterRequest } from '../../shared/models/auth.model';

// Serviço responsável pela comunicação de autenticação com a API Backend (Spring Boot).
// Resolve o problema de isolamento da lógica de rede, centralizando as requisições HTTP
// para os endpoints de login e registro, evitando duplicação de chamadas nos componentes.
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/api/auth`;

  // Envia as credenciais do usuário para a API e retorna o token JWT em caso de sucesso.
  public login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials);
  }

  // Envia os dados de um novo usuário para registro na plataforma.
  // O backend Spring Boot retorna uma String de sucesso (ResponseEntity<String>),
  // por isso definimos o responseType como 'text' para o Angular não tentar fazer parse de JSON.
  public register(data: RegisterRequest): Observable<string> {
    return this.http.post(`${this.API_URL}/register`, data, { responseType: 'text' });
  }
}
