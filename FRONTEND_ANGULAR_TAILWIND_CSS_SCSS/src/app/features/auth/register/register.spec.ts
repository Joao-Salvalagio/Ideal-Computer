import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';

// Importa o nome correto da classe que nós definimos no register.ts
import { RegisterComponent } from './register';

// Suíte de testes unitários do Componente de Registro.
// Garante que a tela seja renderizada corretamente e que as dependências
// vitais (como serviços de rede e rotas) sejam mockadas no ambiente isolado.
describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        // Fornece módulos falsos de rotas e HTTP para o teste não quebrar
        provideHttpClient(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  // Teste de sanidade: Verifica se a tela é criada com sucesso na memória
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
