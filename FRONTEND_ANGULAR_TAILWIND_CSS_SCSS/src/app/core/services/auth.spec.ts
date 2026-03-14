import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

// Importa a classe correta do nosso serviço
import { AuthService } from './auth';

// Suíte de testes unitários do Serviço de Autenticação.
// Resolve o problema de regressão de código, garantindo que a comunicação
// com a API Backend (Java) possa ser simulada e testada isoladamente.
describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      // Injetamos os provedores de HTTP nativos do Angular para testes.
      // Isso impede que o teste tente fazer requisições reais para localhost:8080
      providers: [AuthService, provideHttpClient(), provideHttpClientTesting()],
    });
    // Injeta a instância do serviço no nosso ambiente de teste
    service = TestBed.inject(AuthService);
  });

  // Teste básico de sanidade: verifica se o serviço foi instanciado em memória sem erros
  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
