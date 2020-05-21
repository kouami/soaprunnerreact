import React, {Component} from 'react';

import Header from "./Header";
import URLAddressBar from "./URLAddressBar";
import InputMessageArea from "./InputMessageArea";
import OutputMessageArea from "./OutputMessageArea";
import beautify from 'xml-beautifier'
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.css';


class MainLayout extends Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.state = {
            address: '',
            messageRequest: '',
            messageResponse: ''
        }

    }

    handleSubmit = event => {
        event.preventDefault();

        const data = new FormData(event.target);

        axios.post('http://localhost:8080', data)
            .then(response => {
                this.setState({
                    messageResponse: beautify(response.data.messageResponse)
                })
            }).catch(error => {
            console.log(error);
        })

    }


    myChangeHandler = (event) => {
        let nam = event.target.name;
        let val = event.target.value;
        this.setState({[nam]: val});
    }

    clearMessageResponse = (event) => {

        let nam = event.target.name;
        let val = '';
        this.setState({[nam]: val});
    }


    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="container-sm" style={{backgroundColor: "#F5F5F5", border: "1px solid #000"}}>
                    <div className="row">
                        <div className="col">
                            <Header/>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                          <h2> </h2>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <label htmlFor="exampleFormControlInput1">Endpoint URL Address</label>
                            <input type="text" className="form-control" id="exampleFormControlInput1"
                                   placeholder="http://endpoint-url" name='address' onChange={this.myChangeHandler}>
                            </input>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <label htmlFor="request">Request</label>
                            <textarea className="form-control" id="request" rows="10" cols="20" name='messageRequest' onChange={this.myChangeHandler}></textarea>
                        </div>
                        <div className="col">
                            <label htmlFor="response">Response</label>
                            <textarea className="form-control" id="response" rows="10" cols="20" name='messageResponse' onChange={this.myChangeHandler} value={this.state.messageResponse}></textarea>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <br/>
                            <button className="btn btn-primary" type="submit">Send</button>{' '}
                            <button className="btn btn-secondary" type="reset" onClick={this.clearMessageResponse}>Clear</button>{' '}
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <hr/>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">

                        </div>
                    </div>
                </div>
            </form>
        );
    }
}

export default MainLayout;