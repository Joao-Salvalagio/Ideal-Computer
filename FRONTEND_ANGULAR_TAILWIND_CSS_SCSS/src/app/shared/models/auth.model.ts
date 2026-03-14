// Interface que reflete o AuthRequestDTO do Backend Java.
// Utilizamos 'password' em vez de 'senha_hash' para garantir que o payload
// JSON enviado no body da requisição bata exatamente com os campos esperados pela API.
export interface LoginRequest {
  email: string;
  password: string;
}

// Interface que reflete o AuthResponseDTO do Backend Java.
export interface AuthResponse {
  token: string;
  email: string;
  nome: string;
}

// Interface que reflete os dados necessários para registrar um novo usuário.
// Garante que o payload do Angular cumpra os requisitos do AuthRequestDTO do Java.
export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}
