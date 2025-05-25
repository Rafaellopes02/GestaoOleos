import React, { useState } from 'react';
import SideBar from './../Components/sidebar';
import './Home.css';

/*ICONS*/
import { FaRegNewspaper } from "react-icons/fa";
import { FaTruck } from "react-icons/fa6";
import { RiMoneyDollarCircleFill } from "react-icons/ri";

function Home() {

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
                        <p><b>Contratos Ativos</b></p>
                        <div className="content">
                            <span className="count"><b>150</b></span>
                            <span className="icon-wrapper green">
                                <FaRegNewspaper />
                            </span>
                        </div>
                    </div>

                    <div className="card">
                        <p><b>Recolhas Hoje</b></p>
                        <div className="content">
                            <span className="count"><b>95</b></span>
                            <span className="icon-wrapper orange">
                            <FaTruck />
                        </span>
                        </div>
                    </div>

                    <div className="card">
                        <p><b>Pagamento Pendente</b></p>
                        <div className="content">
                            <span className="count"><b>125,56€</b></span>
                            <span className="icon-wrapper yellow">
                                <RiMoneyDollarCircleFill />
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home;
