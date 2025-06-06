import React, { useEffect, useState } from 'react';
import SideBar from '../Components/Sidebar/sidebar';
import './Home.css';

import { FaTruck } from "react-icons/fa6";
import { RiMoneyDollarCircleFill } from "react-icons/ri";
import { jwtDecode } from "jwt-decode";
import axios from 'axios';

import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer
} from 'recharts';

function Home() {
    const [totalRecolhas, setTotalRecolhas] = useState(0);
    const [totalGanho, setTotalGanho] = useState(0);
    const [ganhosPorMes, setGanhosPorMes] = useState([]);

    useEffect(() => {
        const fetchDados = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) return;

                const decoded = jwtDecode(token);
                const idUtilizador = parseInt(decoded.sub, 10);

                const recolhasResponse = await axios.get('http://localhost:8080/Recolhas');
                const recolhasDoUtilizador = recolhasResponse.data.filter(r => r.idutilizador === idUtilizador);
                setTotalRecolhas(recolhasDoUtilizador.length);

                const contratosResponse = await axios.get('http://localhost:8080/Contratos/com-estado');
                const contratosDoUtilizador = contratosResponse.data.filter(c => c.idutilizador === idUtilizador);
                const idsContratos = contratosDoUtilizador.map(c => c.idcontrato);

                const pagamentosResponse = await axios.get('http://localhost:8080/api/pagamentos');
                const pagamentosDoUtilizador = pagamentosResponse.data.filter(p => idsContratos.includes(p.idcontrato));

                // Total ganho
                const total = pagamentosDoUtilizador.reduce((sum, p) => sum + parseFloat(p.valor || 0), 0);
                setTotalGanho(total.toFixed(2));

                // Agrupar ganhos por mês
                const ganhosMap = {};

                pagamentosDoUtilizador.forEach(p => {
                    if (!p.datapagamento) return;

                    const data = new Date(p.datapagamento);
                    const mesAno = `${data.getMonth() + 1}/${data.getFullYear()}`;

                    if (!ganhosMap[mesAno]) {
                        ganhosMap[mesAno] = 0;
                    }

                    ganhosMap[mesAno] += parseFloat(p.valor || 0);
                });

                const ganhosArray = Object.entries(ganhosMap).map(([mes, valor]) => ({
                    mes,
                    valor: parseFloat(valor.toFixed(2))
                })).sort((a, b) => {
                    // Ordenar por data real
                    const [ma, aa] = a.mes.split("/").map(Number);
                    const [mb, ab] = b.mes.split("/").map(Number);
                    return aa !== ab ? aa - ab : ma - mb;
                });

                setGanhosPorMes(ganhosArray);
            } catch (error) {
                console.error("Erro ao carregar dados do dashboard:", error);
            }
        };

        fetchDados();
    }, []);

    return (
        <div>
            <SideBar />
            <div className="main-content">
                <div className="header">
                    <h1>Dashboard</h1>
                    <p className="subtitle">Bem-vindo</p>
                </div>

                <div className="cards-row">
                    <div className="card">
                        <p><b>Total de Recolhass</b></p>
                        <div className="content">
                            <span className="count"><b>{totalRecolhas}</b></span>
                            <span className="icon-wrapper orange">
                                <FaTruck />
                            </span>
                        </div>
                    </div>

                    <div className="card">
                        <p><b>Total Ganho</b></p>
                        <div className="content">
                            <span className="count"><b>{totalGanho}€</b></span>
                            <span className="icon-wrapper green">
                                <RiMoneyDollarCircleFill />
                            </span>
                        </div>
                    </div>
                </div>

                <div className="grafico-container">
                    <h2>Ganhos por Mês</h2>
                    <ResponsiveContainer width="100%" height={300}>
                        <LineChart data={ganhosPorMes}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="mes" />
                            <YAxis unit="€" />
                            <Tooltip />
                            <Line type="monotone" dataKey="valor" stroke="#82ca9d" strokeWidth={2} />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
}

export default Home;
