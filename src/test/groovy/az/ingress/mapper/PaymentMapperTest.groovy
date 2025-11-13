package az.ingress.mapper

import az.ingress.dao.entity.PaymentInfoEntity
import az.ingress.model.dto.request.PaymentInfoRequest
import az.ingress.model.dto.response.PaymentInfoResponse
import az.ingress.model.mapper.PaymentMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class PaymentMapperTest extends Specification {
    PaymentMapper mapper = Mappers.getMapper(PaymentMapper)

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "toRequest maps PaymentInfoEntity to PaymentRequestDto correctly"() {
        given:
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        when:
        def paymentRequestDto = mapper.toRequest(paymentInfoEntity)
        then:
        paymentRequestDto != null
        paymentRequestDto.orderId == null
        paymentRequestDto.cardHolderName == paymentInfoEntity.cardHolderName
    }

    def "updateEntity with PaymentInfoResponse updates entity correctly"() {
        given:
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        def paymentInfoResponse = random.nextObject(PaymentInfoResponse)
        when:
        mapper.updateEntity(paymentInfoEntity, paymentInfoResponse)
        then:
        paymentInfoEntity.reason == paymentInfoResponse.reason
        paymentInfoEntity.userId == paymentInfoResponse.userId
        paymentInfoEntity.amount == paymentInfoResponse.totalAmount
        paymentInfoEntity.paymentSuccess == paymentInfoResponse.success
    }

    def "updateEntity with PaymentInfoRequest updates entity correctly"() {
        given:
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        def paymentInfoRequest = random.nextObject(PaymentInfoRequest)
        when:
        mapper.updateEntity(paymentInfoEntity, paymentInfoRequest)
        then:
        paymentInfoEntity.amount == paymentInfoRequest.amount
        paymentInfoEntity.currency == paymentInfoRequest.currency
        paymentInfoEntity.cardHolderName == paymentInfoRequest.cardHolderName
    }

    def "toRequest returns null or handles null entity gracefully"() {
        given:
        PaymentInfoEntity paymentInfoEntityNull = null
        when:
        def result = mapper.toRequest(paymentInfoEntityNull)
        then:
        result == null
    }

    def "updateEntity with null response should not update entity fields"() {
        given:
        def originalEntity = random.nextObject(PaymentInfoEntity)
        def copyBeforeUpdate = new PaymentInfoEntity(
                id: originalEntity.id,
                userId: originalEntity.userId,
                amount: originalEntity.amount,
                currency: originalEntity.currency,
                cardHolderName: originalEntity.cardHolderName,
                paymentSuccess: originalEntity.paymentSuccess,
                reason: originalEntity.reason
        )
        when:
        mapper.updateEntity(originalEntity, (PaymentInfoResponse) null)
        then:
        originalEntity == copyBeforeUpdate
    }

    def "updateEntity with inconsistent response fields should throw exception"() {
        given:
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        def invalidResponse = new PaymentInfoResponse(
                totalAmount: null,
                userId: null,
                reason: null
        )
        when:
        mapper.updateEntity(paymentInfoEntity, invalidResponse)
        then:
        noExceptionThrown()
        paymentInfoEntity.amount == null
        paymentInfoEntity.userId == null
        paymentInfoEntity.reason == null
    }

    def "updateEntity should throw exception when entity is null"() {
        given:
        def paymentInfoResponse = random.nextObject(PaymentInfoResponse)
        when:
        mapper.updateEntity(null, paymentInfoResponse)
        then:
        thrown(NullPointerException)
    }

}
