import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const NotFound = () => {
    const navigate = useNavigate();

    return (
        <Container className="text-center d-flex flex-column justify-content-center align-items-center vh-100">
            <Row>
                <Col>
                    <h1 className="display-1 text-danger">404</h1>
                    <h2 className="mb-4">Oops! Page Not Found</h2>
                    <p className="lead mb-5">
                        The page you're looking for doesn't exist or has been moved.
                    </p>
                    <Button onClick={() => navigate(-1)} variant="primary" size="lg">
                        Go Back
                    </Button>
                </Col>
            </Row>
        </Container>
    );
};

export default NotFound;
