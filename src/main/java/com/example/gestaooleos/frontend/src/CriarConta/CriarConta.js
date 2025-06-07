import React, { useState } from 'react';
import './CriarConta.css';

function CriarConta() {
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [street, setStreet] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [password1, setPassword1] = useState('');


    const handleSubmit = async (e) => {
        e.preventDefault();

        if (password !== password1) {
            alert("As passwords não coincidem.");
            return;
        }

        const novoUtilizador = {
            nome: name,
            telefone: phone,
            morada: street,
            username: username,
            password: password,
            idtipoutilizador: 1
        };

        try {
            const response = await fetch('http://localhost:8080/Utilizadores', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(novoUtilizador)
            });

            if (response.ok) {
                alert("Conta criada com sucesso!");
                window.location.href = "/Login";
            } else {
                const erro = await response.text();
                alert("Erro ao criar conta: " + erro);
            }
        } catch (error) {
            console.error("Erro:", error);
            alert("Erro ao comunicar com o servidor.");
        }
    };

    return (
        <div className="criarconta-screen">
            <div className="criarconta-card">
                <img src="/images/img.png" alt="Logo" className="logo" />
                <img src="/images/letras.png" alt="Logo" className="logo" />
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Nome"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Telefone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Morada"
                        value={street}
                        onChange={(e) => setStreet(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Repita a Password"
                        value={password1}
                        onChange={(e) => setPassword1(e.target.value)}
                    />
                    <button type="submit">CRIAR CONTA</button>
                </form>
                <p className="login-text">
                    Já tem conta? <a href="/Login">Faça Login</a>
                </p>
            </div>
        </div>
    );
}

export default CriarConta;
